import React, { useEffect } from "react";

const SuccessPage = () => {
  useEffect(() => {
    // 서버에 사용자 정보 요청
    fetch("http://localhost:8080/api/user/login", {
      method: "POST",
      credentials: "include", // 쿠키를 요청에 포함
    })
      .then((response) => response.json())
      .then((data) => {
        // 서버로부터 받은 사용자 데이터 처리
        console.log(data);
      })
      .catch((error) => {
        console.error("Error fetching data", error);
      });
  }, []);

  return <div>소셜로그인 성공</div>;
};

export default SuccessPage;
