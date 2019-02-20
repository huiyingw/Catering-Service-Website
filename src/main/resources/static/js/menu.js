function MenuDisplay(){
	var displayer = document.getElementById("menu_shower");
	var value = document.getElementById("themeName").value;
	//document.getElementById("menu_shower").innerHTML = "You selected: " + value;
	if (value=='Blank' || value=="Other"){
		displayer.classList.add("hide");
		console.log('it is blank or other');
	}else{
		if (value=='Chinese'){
			displayer.classList.remove("hide");
			displayer.innerHTML = "<b>Menu Selected [Chinese]:</b>"+"<br />"
								+"<b>Appetizers:</b>" + "<br />"+"      Fried Scallops, Pork Egg Roll" + "<br />"
								+"<b>Main Courses:</b>" + "<br />"+"      Peking Duck Tacos, Veggie Kung Po, Beef Shanghai Noodles, Veggie Fried Rice"+"<br />"
								+"<b>Desserts:</b>" + "<br />"+" Minu Donuts, Mango puddings";

		}
		if (value=='French'){
			displayer.classList.remove("hide");
			displayer.innerHTML = "<b>Menu Selected [French]:</b>"+"<br />"
								+"<b>Appetizers:</b>" + "<br />"+"      Roasted Sea Scallops, Frisee and Apple Salad" + "<br />"
								+"<b>Main Courses:</b>" + "<br />"+"      Beef Sirloin Steak, Grilled Salmon, Bangers and Mash, Veggie Stone Pie"+"<br />"
								+"<b>Desserts:</b>" + "<br />"+" Creme brulee, Hot Chocolate Bomb";
		}
		if (value=='American'){
			displayer.classList.remove("hide");
			displayer.innerHTML = "<b>Menu Selected [American]:</b>"+"<br />"
								+"<b>Appetizers:</b>" + "<br />"+"      Yum Fries, Chicken Wings" + "<br />"
								+"<b>Main Courses:</b>" + "<br />"+"      Beef Bacon Burgers, Veggie Burgers, HotDogs"+"<br />"
								+"<b>Desserts:</b>" + "<br />"+" Ice Cream";

		}
		if (value=='Mexcian'){
			displayer.classList.remove("hide");
			displayer.innerHTML = "<b>Menu Selected [Mexcian]:</b>"+"<br />"
								+"<b>Appetizers:</b>" + "<br />"+"      Beef Nachos, Veggie Nachos, Black Bean Soup" + "<br />"
								+"<b>Main Courses:</b>" + "<br />"+"      Beef Tacos, Chicken Burritos, Pork Fajitas"+"<br />"
								+"<b>Desserts:</b>" + "<br />"+" Cookies";

		}
	}
}