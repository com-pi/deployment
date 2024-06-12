db = db.getSiblingDB('auth'); // 'mydatabase' 데이터베이스 생성 또는 선택
db.createCollection('member'); // 'mycollection' 컬렉션 생성

db.member.insert([
    { id: 1, email: "comppi.comppi@gmail.com", phone_number: "01012345678", role: "ADMIN", nickname: "관리자"}
]);
