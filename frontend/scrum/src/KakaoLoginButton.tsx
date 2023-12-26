import React, { useState } from 'react';

interface Props {
}

const KakaoLoginButton: React.FC<Props> = ({}) => {

  const KAKAO_OAUTH_URL = `http://localhost:8080/oauth2/authorization/kakao`; // 로컬
	// const KAKAO_OAUTH_URL = `https://j9b302.p.ssafy.io/oauth2/authorization/kakao`; 서버
	

  const redirectToKakaoLogin = () => {
    window.location.href = KAKAO_OAUTH_URL;
  }; 


  return <button onClick={redirectToKakaoLogin}>카카오로 로그인</button>;
};

export default KakaoLoginButton;