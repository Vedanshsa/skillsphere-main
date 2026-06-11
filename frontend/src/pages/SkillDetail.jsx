import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { skillAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import UserCard from '../components/UserCard';
import { GraduationCap, Users, Plus, Check, ArrowLeft } from 'lucide-react';

const SkillDetail = () => {
  const { id } = useParams();
  const { user, refreshUser } = useAuth();
  const [skill, setSkill] = useState(null);
  const [teachers, setTeachers] = useState([]);
  const [learners, setLearners] = useState([]);
  const [activeTab, setActiveTab] = useState('teachers');
  const [loading, setLoading] = useState(true);
  const [adding, setAdding] = useState(false);

  const isTeaching = user?.skillsTeaching?.some(s => s.skillId === parseInt(id));
  const isLearning = user?.skillsLearning?.some(s => s.skillId === parseInt(id));

  useEffect(() => {
    loadSkillData();
  }, [id]);

  const loadSkillData = async () => {
    try {
      const [skillRes, teachersRes, learnersRes] = await Promise.all([
        skillAPI.getById(id),
        skillAPI.getTeachers(id),
        skillAPI.getLearners(id)
      ]);
      setSkill(skillRes.data);
      setTeachers(teachersRes.data);
      setLearners(learnersRes.data);
    } catch (error) {
      console.error('Failed to load skill:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddSkill = async (type) => {
    if (!user) return;
    setAdding(true);
    try {
      await skillAPI.addToProfile({
        skillId: parseInt(id),
        skillType: type,
        proficiencyNote: ''
      });
      await refreshUser();
      await loadSkillData();
    } catch (error) {
      console.error('Failed to add skill:', error);
    } finally {
      setAdding(false);
    }
  };

  const handleRemoveSkill = async (type) => {
    if (!user) return;
    setAdding(true);
    try {
      await skillAPI.removeFromProfile(id, type);
      await refreshUser();
      await loadSkillData();
    } catch (error) {
      console.error('Failed to remove skill:', error);
    } finally {
      setAdding(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!skill) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8 text-center">
        <p className="text-gray-500">Skill not found</p>
        <Link to="/skills" className="text-primary-600 hover:underline">Back to skills</Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <Link to="/skills" className="inline-flex items-center text-gray-600 hover:text-primary-600 mb-6">
        <ArrowLeft className="h-4 w-4 mr-1" />
        Back to skills
      </Link>

      {/* Skill Header */}
      <div className="card mb-8">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          <div>
            <div className="flex items-center gap-3 mb-2">
              <h1 className="text-3xl font-bold text-gray-900">{skill.name}</h1>
              <span className="category-badge">{skill.category}</span>
            </div>
            {skill.description && (
              <p className="text-gray-600">{skill.description}</p>
            )}
            <div className="flex items-center space-x-6 mt-4 text-sm text-gray-500">
              <div className="flex items-center space-x-1">
                <GraduationCap className="h-5 w-5 text-green-600" />
                <span>{skill.teacherCount} teachers</span>
              </div>
              <div className="flex items-center space-x-1">
                <Users className="h-5 w-5 text-blue-600" />
                <span>{skill.learnerCount} learners</span>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          {user && (
            <div className="flex flex-col sm:flex-row gap-2">
              {isTeaching ? (
                <button
                  onClick={() => handleRemoveSkill('TEACHING')}
                  disabled={adding}
                  className="btn-secondary flex items-center justify-center space-x-2"
                >
                  <Check className="h-4 w-4 text-green-600" />
                  <span>Teaching</span>
                </button>
              ) : (
                <button
                  onClick={() => handleAddSkill('TEACHING')}
                  disabled={adding}
                  className="btn-primary flex items-center justify-center space-x-2"
                >
                  <Plus className="h-4 w-4" />
                  <span>I can teach this</span>
                </button>
              )}
              {isLearning ? (
                <button
                  onClick={() => handleRemoveSkill('LEARNING')}
                  disabled={adding}
                  className="btn-secondary flex items-center justify-center space-x-2"
                >
                  <Check className="h-4 w-4 text-blue-600" />
                  <span>Learning</span>
                </button>
              ) : (
                <button
                  onClick={() => handleAddSkill('LEARNING')}
                  disabled={adding}
                  className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-lg transition-colors flex items-center justify-center space-x-2"
                >
                  <Plus className="h-4 w-4" />
                  <span>I want to learn</span>
                </button>
              )}
            </div>
          )}
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200 mb-6">
        <div className="flex space-x-8">
          <button
            onClick={() => setActiveTab('teachers')}
            className={`pb-4 px-1 border-b-2 font-medium text-sm transition-colors ${
              activeTab === 'teachers'
                ? 'border-primary-600 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            Teachers ({teachers.length})
          </button>
          <button
            onClick={() => setActiveTab('learners')}
            className={`pb-4 px-1 border-b-2 font-medium text-sm transition-colors ${
              activeTab === 'learners'
                ? 'border-primary-600 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            Learners ({learners.length})
          </button>
        </div>
      </div>

      {/* Users List */}
      <div className="grid md:grid-cols-2 gap-4">
        {(activeTab === 'teachers' ? teachers : learners).length === 0 ? (
          <p className="text-gray-500 col-span-2 text-center py-8">
            No {activeTab} yet. Be the first!
          </p>
        ) : (
          (activeTab === 'teachers' ? teachers : learners).map((u) => (
            <UserCard key={u.id} user={u} />
          ))
        )}
      </div>
    </div>
  );
};

export default SkillDetail;