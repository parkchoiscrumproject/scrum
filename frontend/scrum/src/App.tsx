import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import KakaoLoginButton from './KakaoLoginButton.tsx';
import Success from './Success.tsx';
import RegistPage from './pages/RegistPage.tsx';

const LoginButtons: React.FC = () => {
  return (
    <div>
      <KakaoLoginButton />
    </div>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginButtons />} />
        <Route path="/success" element={<Success />} />
        <Route path="/regist" element={<RegistPage />} />
      </Routes>
    </Router>
  );
};

export default App;