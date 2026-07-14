import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { userAPI, skillAPI } from '../services/api';
import { User, Mail, Edit2, X, GraduationCap, BookOpen, Trash2 } from 'lucide-react';
import { Link } from 'react-router-dom';

const Profile = () => {
  const { user, refreshUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [editData, setEditData] = useState({ name: '', bio: '' });
  const [saving, setSaving] = useState(false);
  const [removing, setRemoving] = useState(null);

  const handleEdit = () => {
    setEditData({ name: user.name, bio: user.bio || '' });
    setIsEditing(true);
  };

  const handleSave = async () => {
    setSaving(true);
    try {
      await userAPI.updateMe(editData);
      await refreshUser();
      setIsEditing(false);
    } catch (error) {
      console.error('Failed to update profile:', error);
    } finally {
      setSaving(false);
    }
  };

  const handleRemoveSkill = async (skillId, type) => {
    setRemoving(`${skillId}-${type}`);
    try {
      await skillAPI.removeFromProfile(skillId, type);
      await refreshUser();
    } catch (error) {
      console.error('Failed to remove skill:', error);
    } finally {
      setRemoving(null);
    }
  };

  if (!user) return null;

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      {/* Profile Header */}
      <div className="card mb-8">
        <div className="flex items-start justify-between">
          <div className="flex items-center space-x-4">
            <div className="h-20 w-20 rounded-full bg-primary-100 flex items-center justify-center">
              {user.profilePictureUrl ? (
                <img src={user.profilePictureUrl} alt={user.name} className="h-20 w-20 rounded-full object-cover" />
              ) : (
                <User className="h-10 w-10 text-primary-600" />
              )}
            </div>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{user.name}</h1>
              <p className="text-gray-500 flex items-center space-x-1">
                <Mail className="h-4 w-4" />
                <span>{user.email}</span>
              </p>
              {user.bio && <p className="text-gray-600 mt-2">{user.bio}</p>}
            </div>
          </div>
          <button onClick={handleEdit} className="btn-secondary flex items-center space-x-1">
            <Edit2 className="h-4 w-4" />
            <span>Edit</span>
          </button>
        </div>
      </div>

      {/* Skills Sections */}
      <div className="grid md:grid-cols-2 gap-8">
        {/* Teaching */}
        <div>
          <div className="flex items-center space-x-2 mb-4">
            <GraduationCap className="h-6 w-6 text-green-600" />
            <h2 className="text-xl font-semibold">Skills I Teach</h2>
          </div>
          {user.skillsTeaching?.length === 0 ? (
            <div className="card text-center py-8">
              <p className="text-gray-500 mb-4">You haven't added any teaching skills yet</p>
              <Link to="/skills" className="text-primary-600 hover:underline">Browse skills to add</Link>
            </div>
          ) : (
            <div className="space-y-3">
              {user.skillsTeaching?.map((skill) => (
                <div key={skill.skillId} className="card flex items-center justify-between py-4">
                  <div>
                    <Link to={`/skills/${skill.skillId}`} className="font-medium text-gray-900 hover:text-primary-600">
                      {skill.skillName}
                    </Link>
                    <p className="text-sm text-gray-500">{skill.category}</p>
                    {skill.proficiencyNote && (
                      <p className="text-sm text-gray-600 mt-1">{skill.proficiencyNote}</p>
                    )}
                  </div>
                  <button
                    onClick={() => handleRemoveSkill(skill.skillId, 'TEACHING')}
                    disabled={removing === `${skill.skillId}-TEACHING`}
                    className="text-gray-400 hover:text-red-600 transition-colors"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Learning */}
        <div>
          <div className="flex items-center space-x-2 mb-4">
            <BookOpen className="h-6 w-6 text-blue-600" />
            <h2 className="text-xl font-semibold">Skills I'm Learning</h2>
          </div>
          {user.skillsLearning?.length === 0 ? (
            <div className="card text-center py-8">
              <p className="text-gray-500 mb-4">You haven't added any learning goals yet</p>
              <Link to="/skills" className="text-primary-600 hover:underline">Browse skills to add</Link>
            </div>
          ) : (
            <div className="space-y-3">
              {user.skillsLearning?.map((skill) => (
                <div key={skill.skillId} className="card flex items-center justify-between py-4">
                  <div>
                    <Link to={`/skills/${skill.skillId}`} className="font-medium text-gray-900 hover:text-primary-600">
                      {skill.skillName}
                    </Link>
                    <p className="text-sm text-gray-500">{skill.category}</p>
                    {skill.proficiencyNote && (
                      <p className="text-sm text-gray-600 mt-1">{skill.proficiencyNote}</p>
                    )}
                  </div>
                  <button
                    onClick={() => handleRemoveSkill(skill.skillId, 'LEARNING')}
                    disabled={removing === `${skill.skillId}-LEARNING`}
                    className="text-gray-400 hover:text-red-600 transition-colors"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Edit Modal */}
      {isEditing && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold">Edit Profile</h2>
              <button onClick={() => setIsEditing(false)} className="text-gray-400 hover:text-gray-600">
                <X className="h-6 w-6" />
              </button>
            </div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                <input
                  type="text"
                  value={editData.name}
                  onChange={(e) => setEditData({ ...editData, name: e.target.value })}
                  className="input-field"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Bio</label>
                <textarea
                  value={editData.bio}
                  onChange={(e) => setEditData({ ...editData, bio: e.target.value })}
                  className="input-field min-h-[100px]"
                  placeholder="Tell others about yourself..."
                />
              </div>
              <div className="flex space-x-3">
                <button onClick={() => setIsEditing(false)} className="flex-1 btn-secondary">
                  Cancel
                </button>
                <button onClick={handleSave} disabled={saving} className="flex-1 btn-primary">
                  {saving ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;