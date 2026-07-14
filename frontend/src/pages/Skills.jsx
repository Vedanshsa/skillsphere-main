import { useState, useEffect } from 'react';
import { skillAPI } from '../services/api';
import SkillCard from '../components/SkillCard';
import { Search, Filter } from 'lucide-react';

const Skills = () => {
  const [skills, setSkills] = useState([]);
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadInitialData();
  }, []);

  useEffect(() => {
    if (searchQuery) {
      searchSkills();
    } else if (selectedCategory) {
      loadSkillsByCategory();
    } else {
      loadAllSkills();
    }
  }, [selectedCategory, searchQuery]);

  const loadInitialData = async () => {
    try {
      const [skillsRes, categoriesRes] = await Promise.all([
        skillAPI.getAll(),
        skillAPI.getCategories()
      ]);
      setSkills(skillsRes.data);
      setCategories(categoriesRes.data);
    } catch (error) {
      console.error('Failed to load skills:', error);
    } finally {
      setLoading(false);
    }
  };

  const loadAllSkills = async () => {
    try {
      const response = await skillAPI.getAll();
      setSkills(response.data);
    } catch (error) {
      console.error('Failed to load skills:', error);
    }
  };

  const loadSkillsByCategory = async () => {
    try {
      const response = await skillAPI.getByCategory(selectedCategory);
      setSkills(response.data);
    } catch (error) {
      console.error('Failed to load skills by category:', error);
    }
  };

  const searchSkills = async () => {
    try {
      const response = await skillAPI.search(searchQuery);
      setSkills(response.data);
    } catch (error) {
      console.error('Failed to search skills:', error);
    }
  };

  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
    setSelectedCategory('');
  };

  const handleCategoryChange = (category) => {
    setSelectedCategory(category);
    setSearchQuery('');
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Browse Skills</h1>
        <p className="text-gray-600">Discover skills to learn or find people to teach</p>
      </div>

      {/* Search and Filter */}
      <div className="flex flex-col md:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
          <input
            type="text"
            value={searchQuery}
            onChange={handleSearch}
            placeholder="Search skills..."
            className="input-field pl-10"
          />
        </div>
        <div className="flex items-center space-x-2">
          <Filter className="h-5 w-5 text-gray-400" />
          <select
            value={selectedCategory}
            onChange={(e) => handleCategoryChange(e.target.value)}
            className="input-field"
          >
            <option value="">All Categories</option>
            {categories.map((category) => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Skills Grid */}
      {skills.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">No skills found</p>
          <p className="text-gray-400">Try a different search or category</p>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {skills.map((skill) => (
            <SkillCard key={skill.id} skill={skill} />
          ))}
        </div>
      )}
    </div>
  );
};

export default Skills;