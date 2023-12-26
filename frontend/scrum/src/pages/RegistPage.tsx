import React, { useState } from 'react';

const RegistPage: React.FC = () => {
    const [nickname, setNickname] = useState('');
    const [profileImage, setProfileImage] = useState<File | null>(null);

    const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setNickname(e.target.value);
    };

    const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            setProfileImage(e.target.files[0]);
        }
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        // 여기서 닉네임과 이미지 데이터를 처리하는 로직을 추가합니다.
    };

    return (
        <form onSubmit={handleSubmit}>
            <input 
                type="text" 
                value={nickname} 
                onChange={handleNicknameChange} 
                placeholder="닉네임" 
            />
            <input 
                type="file" 
                onChange={handleImageChange} 
            />
            <button type="submit">등록</button>
        </form>
    );
};

export default RegistPage;
