import { useState, useEffect } from 'react';
import { userAPI, skillAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import UserCard from '../components/UserCard';
import { Users, Sparkles, ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';

const Matches = () => {
  const { user } = useAuth();
  const [mutualMatches, setMutualMatches] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('mutual');

  useEffect(() => {
    loadMatches();
  }, []);

  const loadMatches = async () => {
    try {
      const [matchesRes, recsRes] = await Promise.all([
        userAPI.getMatches(),
        skillAPI.getRecommendations()
      ]);
      setMutualMatches(matchesRes.data);
      setRecommendations(recsRes.data);
    } catch (error) {
      console.error('Failed to load matches:', error);
    } finally {
      setLoading(false);
    }
  };

  const hasSkills = user?.skillsTeaching?.length > 0 || user?.skillsLearning?.length > 0;

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!hasSkills) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-16 text-center">
        <div className="w-20 h-20 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-6">
          <Users className="h-10 w-10 text-primary-600" />
        </div>
        <h1 className="text-2xl font-bold text-gray-900 mb-4">Add skills to find matches</h1>
        <p className="text-gray-600 mb-8">
          To find people to exchange skills with, you need to add at least one skill you can teach 
          and one skill you want to learn.
        </p>
        <Link to="/skills" className="btn-primary inline-flex items-center space-x-2">
          <span>Browse Skills</span>
          <ArrowRight className="h-4 w-4" />
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Your Matches</h1>
        <p className="text-gray-600">People who can help you learn and want to learn from you</p>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200 mb-6">
        <div className="flex space-x-8">
          <button
            onClick={() => setActiveTab('mutual')}
            className={`pb-4 px-1 border-b-2 font-medium text-sm transition-colors flex items-center space-x-2 ${
              activeTab === 'mutual'
                ? 'border-primary-600 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            <Sparkles className="h-4 w-4" />
            <span>Mutual Matches ({mutualMatches.length})</span>
          </button>
          <button
            onClick={() => setActiveTab('recommendations')}
            className={`pb-4 px-1 border-b-2 font-medium text-sm transition-colors flex items-center space-x-2 ${
              activeTab === 'recommendations'
                ? 'border-primary-600 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            <Users className="h-4 w-4" />
            <span>Recommended Teachers ({recommendations.length})</span>
          </button>
        </div>
      </div>

      {/* Content */}
      {activeTab === 'mutual' ? (
        <div>
          {mutualMatches.length === 0 ? (
            <div className="text-center py-12">
              <Sparkles className="h-12 w-12 text-gray-300 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No mutual matches yet</h3>
              <p className="text-gray-500 max-w-md mx-auto">
                Mutual matches are people who teach what you want to learn AND want to learn what you teach. 
                Add more skills to increase your chances!
              </p>
            </div>
          ) : (
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
              {mutualMatches.map((match) => (
                <UserCard key={match.id} user={match} />
              ))}
            </div>
          )}
        </div>
      ) : (
        <div>
          {recommendations.length === 0 ? (
            <div className="text-center py-12">
              <Users className="h-12 w-12 text-gray-300 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No recommendations yet</h3>
              <p className="text-gray-500 max-w-md mx-auto">
                Add skills you want to learn to see people who can teach them.
              </p>
            </div>
          ) : (
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
              {recommendations.map((rec) => (
                <UserCard key={rec.id} user={rec} />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Matches;