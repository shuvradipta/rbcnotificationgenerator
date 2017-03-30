/**
 * 
 */
(function(){
var app = angular.module('serviceNoticeApp',[]);

app.controller('serviceNoticeController',function(){
	this.noticesList = [];
	
	var checkKey = '';
	
	/*
	 * This function will add the key to the array if not present and if present will remove it
	 */
	this.manageNotice = function(noticeKey){
		console.log(noticeKey);
		checkKey = noticeKey;
		var keyIndex = this.noticesList.findIndex(checkNoticeKey); 
		if ( keyIndex == -1 ){	// If the key is not in the array, add it to the array
			this.noticesList.push(noticeKey);
		}else {		// If the key is in the array, remove it from the array
			this.noticesList.splice(keyIndex,1);
		}
		//console.log('this.noticesList = ' + this.noticesList);
	}
	
	function checkNoticeKey(key) {
		return key == checkKey;
	}
});
})();