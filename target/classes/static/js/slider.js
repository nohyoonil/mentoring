window.addEventListener("load", function(){
    let $sliderList = document.querySelectorAll(".slider-area");
    
    $sliderList.forEach($sliderArea => {
        let $slider = $sliderArea.querySelector(".slider");
        let $sliderItemsWrap = $slider.querySelector(".slider-items-wrap");
        let itemCount = $slider.querySelectorAll(".slider-item").length - 1;    //카드 갯수 -1
        let itemNum = 0;    //현재 보여야 하는 카드가 몇 번째인지
        let startX;         //터치 시작 시 손가락 x좌표
        let endX;           //손가락 떼기 전 손가락 x좌표
        let resultX;        //손가락 움직이는 위치   
        let originPos;      //.slider-item-wrap의 left 위치를 받아옴.

        //손가락 터치
        $slider.addEventListener("touchstart", function(e){
            originPos = ($sliderItemsWrap.style.left).substring(0,($sliderItemsWrap.style.left).indexOf("px"));
            startX =  e.touches[0].clientX;
            $sliderItemsWrap.style.transition="all 0s";
        });

        $slider.addEventListener("touchmove", function(e){
            endX = e.touches[0].clientX;
            resultX = endX - startX;
            $sliderItemsWrap.style.left = (Number(originPos) + Number(resultX)) + "px";
        });
        $slider.addEventListener("touchend", function(e){
            let w = $slider.offsetWidth;
            $sliderItemsWrap.style.transition="all 0.5s";
            resultX = endX - startX;
            if(resultX < 0 && itemNum < itemCount){
                itemNum++;   
            }
            if(resultX > 0 && itemNum > 0){
                itemNum--;
            }
            $sliderItemsWrap.style.left = (itemNum * w * -1)+ "px";
        });

        //버튼으로 컨트롤
        let $prev = $sliderArea.querySelector(".btn-slider-prev");
        let $next = $sliderArea.querySelector(".btn-slider-next");
        let w = $slider.offsetWidth;
        $prev.addEventListener("click", function(e){
            if(itemNum > 0){
                itemNum--;
            }
            $sliderItemsWrap.style.left = (itemNum * w * -1)+ "px";
        });
        $next.addEventListener("click", function(e){
            if(itemNum < itemCount){
                itemNum++;
            }
            $sliderItemsWrap.style.left = (itemNum * w * -1)+ "px";
        });
    });
});