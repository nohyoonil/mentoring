// 윈도우 사이즈
let screenWidth = window.innerWidth;
let screenHeight = window.innerHeight;
// console.log("현재 화면 너비: " + screenWidth + "px");
// console.log("현재 화면 높이: " + screenHeight + "px");

// 스크롤링 한거 기록
let maxScrollTop = 0
let documentHeight = 0;

// 지금 페이지 전체 높이
document.addEventListener('DOMContentLoaded', () => {
    // 전체 페이지의 높이를 측정하는 함수
    const getPageHeight = () => {
        return Math.max(
            document.body.scrollHeight,
            document.documentElement.scrollHeight,
            document.body.offsetHeight,
            document.documentElement.offsetHeight
        );
    };

    // 전체 페이지 높이 가져오기
    documentHeight = getPageHeight();
});


window.onscroll = function() {
    const scrollTop = window.scrollY;
    // 최고 스크롤 위치 업데이트
    if (scrollTop > maxScrollTop) {
        maxScrollTop = scrollTop;
    }
};

window.addEventListener('beforeunload', function (event) {
    var totalScrollingHeight = documentHeight - screenHeight;
    var scrollingPercentage = maxScrollTop / totalScrollingHeight;
    console.log(totalScrollingHeight);
    console.log(scrollingPercentage);
    // POST 요청
    fetch(`/getLog?scrollingPercentage=${encodeURIComponent(scrollingPercentage)}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // 필요시 적절한 Content-Type 사용
        },
    })
        .then(response => response.text())  // 텍스트 형식으로 응답 받기
        .catch(error => {
            // 오류 처리
            console.error('JS send log error : ', error);
        });
});