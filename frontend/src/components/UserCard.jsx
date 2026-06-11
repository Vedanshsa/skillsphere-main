import { Link } from 'react-router-dom';
import { User, Mail } from 'lucide-react';

const UserCard = ({ user, matchedSkills }) => {
  return (
    <Link to={`/users/${user.id}`} className="card hover:border-primary-300 border border-transparent">
      <div className="flex items-start space-x-4">
        <div className="flex-shrink-0">
          {user.profilePictureUrl ? (
            <img
              src={user.profilePictureUrl}
              alt={user.name}
              className="h-12 w-12 rounded-full object-cover"
            />
          ) : (
            <div className="h-12 w-12 rounded-full bg-primary-100 flex items-center justify-center">
              <User className="h-6 w-6 text-primary-600" />
            </div>
          )}
        </div>
        
        <div className="flex-1 min-w-0">
          <h3 className="text-lg font-semibold text-gray-900 truncate">{user.name}</h3>
          <p className="text-sm text-gray-500 truncate">{user.email}</p>
          
          {user.bio && (
            <p className="text-sm text-gray-600 mt-2 line-clamp-2">{user.bio}</p>
          )}
          
          {matchedSkills && matchedSkills.length > 0 && (
            <div className="mt-3 flex flex-wrap gap-2">
              {matchedSkills.map((skill, index) => (
                <span key={index} className="skill-tag text-xs">
                  {skill}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>
    </Link>
  );
};

export default UserCard;