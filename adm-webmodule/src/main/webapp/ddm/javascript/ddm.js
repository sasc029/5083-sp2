var Ddm = function(){};
var ddm = new Ddm();

//发放单详细画面，提交工作流后的回调函数
Ddm.prototype.ddmApproveorderCallBack = function(){
	window.returnValue = true;
	window.close();
};