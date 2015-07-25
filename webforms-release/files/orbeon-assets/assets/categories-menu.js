function getCategoriesList(){
  return document.getElementById("categories_menu").getElementsByTagName("li");
}

function getCategoryAnchorId(category){
  return category.children[0].children[0].getAttribute("id");
}

function getSelectedCategoryIndex(categories){
  for (var i=0; i<categories.length; i++) {
    if(categories[i].getAttribute("class").match("webforms-list-no-bullets-element-selected")){
      return i;
    }
  }
}

function getNextCategoryAnchorId(){
  var categories = getCategoriesList();
  var selectedCategoryIndex = getSelectedCategoryIndex(categories);
  for (var i=selectedCategoryIndex+1; i<categories.length; i++) {
    if(!categories[i].children[0].getAttribute("class").match("category-button-disabled")){
      return getCategoryAnchorId(categories[i]);
    }
  }
  return null;
}

function getPreviousCategoryAnchorId(){
  var categories = getCategoriesList();
  var selectedCategoryIndex = getSelectedCategoryIndex(categories);
  for (var i=selectedCategoryIndex-1; i>=0; i--) {
    if(!categories[i].children[0].getAttribute("class").match("category-button-disabled")){
      return getCategoryAnchorId(categories[i]);
    }
  }
  return null;
}

function clickNextCategory(){
	var nextAnchorId = getNextCategoryAnchorId();
	if(nextAnchorId != null){
		document.getElementById(nextAnchorId).click();
	}
}

function clickPreviousCategory(){
	var previousAnchorId = getPreviousCategoryAnchorId();
	if(previousAnchorId != null){
		document.getElementById(previousAnchorId).click();
	}
}

function disableButton(buttonId){
	document.getElementById(buttonId).children[0].disabled = true;
}

function enableButton(buttonId){
	document.getElementById(buttonId).children[0].disabled = false;
}

function enablePreviousButton(){
	enableButton('previous-button');
}

function disablePreviousButton(){
	disableButton('previous-button');
}

function enableNextButton(){
	enableButton('next-button');
}

function disableNextButton(){
	disableButton('next-button');
}

function enableDisablePreviousNextButtons(){
	// Previous button
	if(getPreviousCategoryAnchorId() != null){
		enablePreviousButton();
	}else{
		disablePreviousButton();
	}
	// Next button
	if(getNextCategoryAnchorId() != null){
		enableNextButton();
	}else{
		disableNextButton();
	}
}


