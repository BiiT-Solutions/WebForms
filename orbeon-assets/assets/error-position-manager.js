// Waits for the document to be loaded
onload = function() {
	// Get a reference to the categories content
	var parentElement = document.getElementById('fr-view');
	var categoriesContentIndex = 5;
	var theFirstChild = parentElement.childNodes[categoriesContentIndex];
	// Get the error element
	var newElement = document.getElementById('error-summary-control-bottom');
	// Insert the new element before the categories div
	parentElement.insertBefore(newElement, theFirstChild);
}