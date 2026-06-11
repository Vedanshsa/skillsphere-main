import { Users, GraduationCap } from 'lucide-react';
import { Link } from 'react-router-dom';

const SkillCard = ({ skill }) => {
  return (
    <Link to={`/skills/${skill.id}`} className="card hover:border-primary-300 border border-transparent">
      <div className="flex justify-between items-start mb-3">
        <h3 className="text-lg font-semibold text-gray-900">{skill.name}</h3>
        <span className="category-badge">{skill.category}</span>
      </div>
      
      {skill.description && (
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">{skill.description}</p>
      )}
      
      <div className="flex items-center space-x-4 text-sm text-gray-500">
        <div className="flex items-center space-x-1">
          <GraduationCap className="h-4 w-4 text-green-600" />
          <span>{skill.teacherCount} teachers</span>
        </div>
        <div className="flex items-center space-x-1">
          <Users className="h-4 w-4 text-blue-600" />
          <span>{skill.learnerCount} learners</span>
        </div>
      </div>
    </Link>
  );
};

export default SkillCard;