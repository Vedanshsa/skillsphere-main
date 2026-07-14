import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { ArrowRight, Users, BookOpen, Sparkles, Target } from 'lucide-react';

const Home = () => {
  const { user } = useAuth();

  return (
    <div>
      {/* Hero Section */}
      <section className="bg-gradient-to-br from-primary-50 to-white py-20 px-4">
        <div className="max-w-7xl mx-auto text-center">
          <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-6">
            Learn from peers.
            <br />
            <span className="text-primary-600">Teach what you know.</span>
          </h1>
          <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto">
            SkillSphere connects people who want to learn with people who can teach. 
            Exchange skills, grow together, and build meaningful connections.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            {user ? (
              <>
                <Link to="/skills" className="btn-primary text-lg px-8 py-3 flex items-center justify-center space-x-2">
                  <span>Browse Skills</span>
                  <ArrowRight className="h-5 w-5" />
                </Link>
                <Link to="/matches" className="btn-secondary text-lg px-8 py-3">
                  Find Matches
                </Link>
              </>
            ) : (
              <>
                <Link to="/register" className="btn-primary text-lg px-8 py-3 flex items-center justify-center space-x-2">
                  <span>Get Started Free</span>
                  <ArrowRight className="h-5 w-5" />
                </Link>
                <Link to="/skills" className="btn-secondary text-lg px-8 py-3">
                  Browse Skills
                </Link>
              </>
            )}
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 px-4">
        <div className="max-w-7xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">
            How SkillSphere Works
          </h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <BookOpen className="h-8 w-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Share Your Skills</h3>
              <p className="text-gray-600">
                List the skills you can teach and the ones you want to learn. 
                From coding to cooking, music to marketing.
              </p>
            </div>
            <div className="text-center">
              <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <Users className="h-8 w-8 text-green-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Find Your Match</h3>
              <p className="text-gray-600">
                Our matching system finds people who teach what you want to learn 
                and want to learn what you can teach.
              </p>
            </div>
            <div className="text-center">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <Sparkles className="h-8 w-8 text-purple-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Grow Together</h3>
              <p className="text-gray-600">
                Connect with your matches, schedule sessions, and start your 
                skill exchange journey. Everyone wins!
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-primary-600 py-16 px-4">
        <div className="max-w-4xl mx-auto text-center">
          <h2 className="text-3xl font-bold text-white mb-4">
            Ready to start learning?
          </h2>
          <p className="text-primary-100 text-lg mb-8">
            Join thousands of people exchanging skills and growing together.
          </p>
          {!user && (
            <Link to="/register" className="bg-white text-primary-600 font-medium px-8 py-3 rounded-lg hover:bg-primary-50 transition-colors inline-flex items-center space-x-2">
              <span>Create Free Account</span>
              <ArrowRight className="h-5 w-5" />
            </Link>
          )}
        </div>
      </section>
    </div>
  );
};

export default Home;