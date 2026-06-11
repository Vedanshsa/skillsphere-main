import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { userAPI } from '../services/api';
import { User, Mail, ArrowLeft, GraduationCap, BookOpen } from 'lucide-react';

const UserProfile = () => {
  const { id } = useParams();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProfile();
  }, [id]);

  const loadProfile = async () => {
    try {
      const response = await userAPI.getUser(id);
      setProfile(response.data);
    } catch (error) {
      console.error('Failed to load profile:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8 text-center">
        <p className="text-gray-500">User not found</p>
        <Link to="/skills" className="text-primary-600 hover:underline">Back to skills</Link>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <Link to="/skills" className="inline-flex items-center text-gray-600 hover:text-primary-600 mb-6">
        <ArrowLeft className="h-4 w-4 mr-1" />
        Back
      </Link>

      {/* Profile Header */}
      <div className="card mb-8">
        <div className="flex items-center space-x-4">
          <div className="h-20 w-20 rounded-full bg-primary-100 flex items-center justify-center">
            {profile.profilePictureUrl ? (
              <img src={profile.profilePictureUrl} alt={profile.name} className="h-20 w-20 rounded-full object-cover" />
            ) : (
              <User className="h-10 w-10 text-primary-600" />
            )}
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{profile.name}</h1>
            <p className="text-gray-500 flex items-center space-x-1">
              <Mail className="h-4 w-4" />
              <span>{profile.email}</span>
            </p>
            {profile.bio && <p className="text-gray-600 mt-2">{profile.bio}</p>}
          </div>
        </div>
      </div>

      {/* Skills */}
      <div className="grid md:grid-cols-2 gap-8">
        {/* Teaching */}
        <div>
          <div className="flex items-center space-x-2 mb-4">
            <GraduationCap className="h-6 w-6 text-green-600" />
            <h2 className="text-xl font-semibold">Can Teach</h2>
          </div>
          {profile.skillsTeaching?.length === 0 ? (
            <p className="text-gray-500">No teaching skills listed</p>
          ) : (
            <div className="space-y-2">
              {profile.skillsTeaching?.map((skill) => (
                <Link
                  key={skill.skillId}
                  to={`/skills/${skill.skillId}`}
                  className="block card py-3 hover:border-green-300 border border-transparent"
                >
                  <span className="font-medium text-gray-900">{skill.skillName}</span>
                  <span className="text-sm text-gray-500 ml-2">{skill.category}</span>
                </Link>
              ))}
            </div>
          )}
        </div>

        {/* Learning */}
        <div>
          <div className="flex items-center space-x-2 mb-4">
            <BookOpen className="h-6 w-6 text-blue-600" />
            <h2 className="text-xl font-semibold">Wants to Learn</h2>
          </div>
          {profile.skillsLearning?.length === 0 ? (
            <p className="text-gray-500">No learning goals listed</p>
          ) : (
            <div className="space-y-2">
              {profile.skillsLearning?.map((skill) => (
                <Link
                  key={skill.skillId}
                  to={`/skills/${skill.skillId}`}
                  className="block card py-3 hover:border-blue-300 border border-transparent"
                >
                  <span className="font-medium text-gray-900">{skill.skillName}</span>
                  <span className="text-sm text-gray-500 ml-2">{skill.category}</span>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UserProfile;