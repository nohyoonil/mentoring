document.addEventListener('DOMContentLoaded', () => {
    // 페이지 URL 가져오기
    const currentUrl = window.location.href;
    console.log('현재 URL:', currentUrl);

    let startTime = performance.now();
});

window.addEventListener('beforeunload', function (event) {
    const endTime = performance.now();
    const timeSpent = (endTime - startTime) / 1000; // 초 단위
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