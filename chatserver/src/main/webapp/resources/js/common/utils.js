function formatTemplate(str, data) {
    Object.keys(data).forEach((key) =>{
      str = replaceAll(str, "{" + key + "}", data[key]);
    });
    return str;
};

function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
};