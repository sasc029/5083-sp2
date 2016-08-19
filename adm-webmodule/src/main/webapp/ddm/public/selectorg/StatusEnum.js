window.StatusEnum=function(value,type)
	{
		this.value=value;
		this.type = type;
	}
window.StatusEnum.prototype.getStatusString=function(){
	if(this.type=='DISMEDIATYPE'){
		for (var i=0;i<disMediaTypeListEditor.length;i++){  
			if(this.value==disMediaTypeListEditor[i].id)
			return disMediaTypeListEditor[i].name;
		}
	}
}
