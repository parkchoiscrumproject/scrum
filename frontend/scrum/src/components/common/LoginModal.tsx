import React, { useState } from "react";
import buttonImage from "../../assets/login_button.png";
import styles from "./LoginModal.module.css";

interface Props {}

const LoginModal: React.FC<Props> = ({}) => {
  const KAKAO_OAUTH_URL = `http://localhost:8080/oauth2/authorization/kakao`; // 로컬
  // const KAKAO_OAUTH_URL = `https://j9b302.p.ssafy.io/oauth2/authorization/kakao`; 서버

  const redirectToKakaoLogin = () => {
    window.location.href = KAKAO_OAUTH_URL;
  };

  return (
    <img
      src={buttonImage}
      onClick={redirectToKakaoLogin}
      className={styles.loginButton}
    ></img>
  );
};

export default LoginModal;
