/*
* 把excel的列头部名("A", "AB"这类)与索引号相互转换(索引是从0开始)
*/

function getBasicColumn() {
    let result = [];
    let start = 65,  // "A"
        end   = 90;  // "Z"
    for (let index = start; index <= end; index++) {
        result.push(String.fromCharCode(index))
    }
    return result;
}

// index 从0开始
function IndexToColumnName(index) {
    const charCount = 26;
    const basicChar = getBasicColumn();
    let result = [];

    let position = index + 1;
    let t = 0;
    do {
        t = position % charCount;
        if (t == 0) {
            t = charCount;
        }
        result.push(basicChar[t - 1]);
        position = (position - t) / charCount;
    } while(position > 0)

    let str = "";
    for (let i = result.length - 1; i >= 0; i--) {
        str += result[i];
    }
    return str
}

// 返回index是从0开始
function ColNumToIndex(col) {
    col = col.toUpperCase();
    let basicChars = getBasicColumn();
    let result = 0;
    let len = col.length;
    for (let i = len - 1; i > -1; i--) {
        let c = basicChars.indexOf(col.charAt(i));
        if (c < 0) {
            return -1;
        }
        let p = len - 1 - i;
        result += (c + 1) * Math.pow(26, p)
    }
    return result - 1;
}
