import React, { useEffect } from "react";
import LoginModal from "../../components/common/LoginModal.tsx";
import logo from "../../assets/logo.png";
import styles from "./Home.module.css";

const Home: React.FC = () => {
  return (
    <div className={styles.container}>
      <img src={logo} alt="Logo" className={styles.logo} />
      <LoginModal></LoginModal>
    </div>
  );
};

export default Home;
