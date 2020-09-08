const BG_IMG_SRC = 'images/bg.jpg'
const BG_WIDTH = 300
const BG_HEIGHT = 300
const screenWidth = window.innerWidth
const screenHeight = window.innerHeight
let rectWidth = parseInt((screenWidth) / 7);
let rectHeight = parseInt((screenHeight) / 7);

let ctx = canvas.getContext('2d');

let atlas = new Image()
atlas.src = BG_IMG_SRC;

/**
 * 页面主函数
 */
export default class Main {
    constructor() {
        // console.info(rectWidth, rectHeight);
        //假设中心的方框的坐标为0，0
        //中心点坐标为
        var x = (screenWidth - rectWidth) / 2;
        var y = (screenHeight - rectHeight) / 2
        ctx.fillStyle = "red";
        // ctx.fillRect(x, y, rectWidth, rectHeight);
        for (var j = -3; j <= 3; j++) {
            for (var i = -3; i <= 3; i++) {
                if (Math.abs(i) + Math.abs(j) <= 3) { //这样才能执行
                    if (j == 0 && i == 0) {
                        ctx.fillStyle = "white";
                    } else {
                        ctx.fillStyle = "red";
                    }
                    ctx.fillRect(x + i * rectWidth, y + j * rectHeight, rectWidth, rectHeight);
                }
            }
        }
        // //四周方框坐标为

        // var num = 0; 
        // var new_x = null;
        //     for(var j=-5;j<6;j++){

        //             new_x = x + j * rectWidth;
        //             ctx.fillRect(new_x, y, rectWidth, rectHeight);
        //             num = num + 1;
        //             console.info(num);
        //     }
        // ctx.fillRect(lefttop, 0, width, height)

        // ctx.fillStyle = "red"
        // ctx.font = "4px Arial"
        // ctx.fillText(
        //     '出五服',
        //     lefttop,
        //     height/2,
        // )
        // ctx.fillText(
        //     '出五服',
        //     screenWidth / 2 - 4,
        //     height / 4,
        // )
        // console.info(atlas，；。l'l'l'l'p'l);
        //** 画背景 */
        // ctx.drawImage(
        //     atlas,
        //     0,
        //     0,
        //     this.BG_WIDTH,
        //     this.BG_HEIGHT,
        //     0,
        //     0,
        //     screenWidth,
        //     screenHeight
        // )
    }

    render(x, y) {
        var left_top_x = (screenWidth - (y + 1) * rectWidth) / 2;
        var left_top_y = x * rectHeight;
        console.info(left_top_x + ", " + left_top_y);
        ctx.fillStyle = "red";
        ctx.fillRect(left_top_x, left_top_y, rectWidth, rectHeight);
    }
}