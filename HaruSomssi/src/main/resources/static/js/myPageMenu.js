
let key = "${param.key}";
console.log(key);
if(key === "userinfo"){
	
	$("#myreview-tab").removeClass("active");
	$("#wishlist-tab").removeClass("active");
	$("#userinfo-tab").addClass("active");
	
	$("#myreview").removeClass("show active");
	$("#wishlist").removeClass("show active");
	$("#userinfo").addClass("show active");
	
}else if(key === "myreview"){
	
	$("#wishlist-tab").removeClass("active");
	$("#userinfo-tab").removeClass("active");
	$("#myreview-tab").addClass("active");
	
	$("#userinfo").removeClass("show active");
	$("#wishlist").removeClass("show active");
	$("#myreview").addClass("show active");
	
}else if(key === "wishlist"){
	
	$("#userinfo-tab").removeClass("active");
	$("#myreview-tab").removeClass("active");
	$("#wishlist-tab").addClass("active");
	
	$("#myreview").removeClass("show active");
	$("#userinfo").removeClass("show active");
	$("#wishlist").addClass("show active");
	
}