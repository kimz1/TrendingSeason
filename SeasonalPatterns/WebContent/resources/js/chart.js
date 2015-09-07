var NS, g, clientXCoord, clientYCoord, posY, _posY, posX, _posX, dayPos, verticalAxis, horizontalAxis, infoBar, horizAxisWidth, boundingRectLeft, boundingRectTop, elementWidth, elementHeight, tickHeight, tickSize, range, dateObject, dateArray, dateCount, halfRange, verticalLine, horizontalLine, priceRect, priceText, priceInfoText, dateInfoText, priceMin, priceMax, priceCell, padding;

if(window.onload) {
	var tempOnload = window.onload;
	var newOnload = function() {
		init();
		tempOnload();
	};
	window.onload = newOnload;
} else {
	window.onload = init;
}

function init() {
	
	NS = "http://www.w3.org/2000/svg";
	verticalAxisDivisor = 10; // Sets the count of divisions made on a vertical axis line.
	verticalAxis = document.getElementById("verticalAxis");
	horizontalAxis = document.getElementById("horizontalAxis");
	chartArea = document.getElementById("chartArea");
	infoBar = document.getElementById("infoBar");
	g = document.getElementById("g");
	verticalLine = document.getElementById("verticalLine");
	horizontalLine = document.getElementById("horizontalLine");
	priceCell = document.getElementById("priceCell");
	priceRect = document.getElementById("priceRect");
	priceText = document.getElementById("priceText");
	elementHeight = Math.ceil(chartArea.height.baseVal.value);
	elementWidth = Math.ceil(chartArea.width.baseVal.value); 
	horizAxisWidth = horizontalAxis.width.baseVal.value;
	dateInfoText = document.getElementById("dateInfoText");
	priceInfoText = document.getElementById("priceInfoText");
	dayPos = 0;
	dateArray = [];
	
	range = parseInt(document.getElementById("utilChart:hiddenForm:hiddenTickRange").value);
	priceMin = parseInt(document.getElementById("utilChart:hiddenForm:hiddenPriceMin").value);
	priceMax = parseInt(document.getElementById("utilChart:hiddenForm:hiddenPriceMax").value);
	padding = parseFloat(document.getElementById("utilChart:hiddenForm:hiddenPadding").value);
	dateCount = parseInt(document.getElementById("utilChart:hiddenForm:hiddenDateCount").value) - 1;
	dateObject = JSON.parse(document.getElementById("utilChart:hiddenForm:hiddenDateObject").value);
	tickSize = parseFloat(document.getElementById("utilChart:hiddenForm:hiddenTickSize").value);
	halfRange = (priceMax / 2.0) - (priceMin / 2.0);
	tickHeight = getTickHeight();
	dateInfoText.innerHTML = "";
	priceInfoText.innerHTML = "";

	chartArea.addEventListener("mousemove", mouseMove, false);
	chartArea.addEventListener("mouseleave", mouseLeave, false);
	chartArea.addEventListener("mouseover", mouseOver, false);
	
	drawVerticalAxis();
	drawHorizontalAxis();
}

function mouseMove(event) {
	clientXCoord = event.clientX;
	clientYCoord = event.clientY;
	boundingRectLeft = chartArea.getBoundingClientRect().left;
	boundingRectTop = chartArea.getBoundingClientRect().top;
	redrawPosition();
}

function mouseLeave() {

	verticalLine.setAttributeNS(null, "class", "hidden");
	horizontalLine.setAttributeNS(null, "class", "hidden");
	priceRect.setAttributeNS(null, "class", "hidden");
	priceText.setAttributeNS(null, "class", "hidden");
	eraseInfoBar();
	
	function eraseInfoBar() {
		dateInfoText.innerHTML = "";
		dateInfoText.setAttribute("x", 0);
		priceInfoText.innerHTML = "";
		priceInfoText.setAttribute("x", 1);
		console.log("erased");
	}
}

function mouseOver() {
	
	verticalLine.setAttributeNS(null, "class", "visible");
	horizontalLine.setAttributeNS(null, "class", "visible");
	priceRect.setAttributeNS(null, "class", "visible");
	priceText.setAttributeNS(null, "class", "visible");
}

var yAxis = {
	getCoord : function(posRatio)  {
	
		/*
		 * Returns the Y position relative to chart area with rounded values
		 * 
		 * 1. Get the current position - posY;
		 * 2. The computation must be relative to chart's centre position;
		 * 3. Get the rounded tick value with regard to padding and tick size;
		 * 4. Return the position rounded to whole ticks;
		 */
		
		
		var halfElementHeight = elementHeight / 2.0;
		var rounded_midBased = yAxis.getRoundedTick(posRatio) - halfRange - priceMin;
		return halfElementHeight + (rounded_midBased * tickHeight);
	},

	getRoundedTick : function(posRatio) {
		var scaledTick_Y = (function() {
			
			/*
			 * Returns the tick position (price) with regard to the padding as a number rounded
			 * to its nearest tick. The breaking line for the computation is in the exact middle
			 * (50%) of the chart. Algorithm is suitable for both the upper and the lower half.
			 */
			
			var scaledTick = (halfRange / ((1 - padding) / 2.0) * (posRatio - 0.5)) + halfRange + priceMin;
			return scaledTick;
		}());
		
		var rounded = round(scaledTick_Y);
		return rounded;
	},
	
	getRatio : function(price) {
		var ratioResult = ((price - halfRange - priceMin) * ((1 - padding) / 2.0) / halfRange) + 0.5; // the reversed of getScaledTick_Y;
		return ratioResult;
	}
};

var xAxis = {
	getCoord : function(currentCoordX)  {
		
		/*
		 * Returns the X position relative to chart area with rounded values
		 * 
		 * 1. Get the current position - posX;
		 * 2. Get the rounded date position value;
		 * 3. Return the rounded X position coordinate;
		 */
		
		var dayLenght = elementWidth / dateCount;
		dayPos = Math.round(currentCoordX / dayLenght); // global var;
		var result = dayPos * dayLenght;
		return result;
	},
};

function redrawPosition() {
	

	posX = (clientXCoord - boundingRectLeft); // get the current position relative to the chartArea upper-left corner
	_posX = xAxis.getCoord(posX); // save the converted position into a variable due to performance increase;
	verticalLine.setAttributeNS(null, "x1", _posX);
	verticalLine.setAttributeNS(null, "x2", _posX);
	
	posY = (clientYCoord - boundingRectTop); // get the current position relative to the chartArea upper-left corner
	var ratioY = posY / elementHeight;
	_posY = yAxis.getCoord(ratioY); // save the converted position into a variable due to performance increase;
	horizontalLine.setAttributeNS(null, "y1", _posY);
	horizontalLine.setAttributeNS(null, "y2", _posY);
	
	if(_posY > 10) {
		priceCell.setAttributeNS(null, "y", _posY
				- (priceCell.getBoundingClientRect().height / 2));
	} else {
		priceCell.setAttributeNS(null, "y", 3);
	}
	
	priceText.innerHTML = yAxis.getRoundedTick(1 - (_posY / elementHeight)); // reverted value (up - higher values, down - lower values);
	updateInfoBar();
}

function drawVerticalAxis() {
	var incrementArray = [10000, 5000, 1000, 500, 100, 50, 20, 10, 5, 2, 1, 0.5, 0.25, 0.1, 0.05, 0.005], // an array of all possible increments;
		increment = getScaleIncrement(incrementArray),
		minimumValue = getAxisValueMinimum(priceMin, increment),
		valueArray = getAxisDivisionValuesArray(minimumValue, priceMax, increment),
		positionArray = scaledPriceToRatio(valueArray);
	
	draw(positionArray);
	
	function getScaleIncrement(incArray) { // finds the smallest division for a vertical axis in ticks;
		var i = 0;
		for(i; i < incArray.length; i++) {
			var scaleIncrement = incArray[i];
			if((range / scaleIncrement) > 5) {
				return scaleIncrement;
			}
		}
	}
	
	function getAxisValueMinimum(minValue, increment) { // gets the smallest value potentionally visible on a vertical axis;
		var minValueResult = increment * Math.floor(minValue / increment);
		return minValueResult;
	}
	
	function getAxisDivisionValuesArray(minValue, maxValue, increment) { // returns an array of vertical axis values;
		var i = 0;
		var division = minValue;
		var resultArray = [];
		for(division; division <= maxValue + increment; i++, division += increment){
			resultArray[i] = minValue + (increment * i);
		}
		return resultArray;
	}
	
	function scaledPriceToRatio(priceArray) {
		var i = 0;
		var ratioArray = [];
		for(i; i < priceArray.length; i++) {
			var ratio = yAxis.getRatio(priceArray[i]);
			ratioArray[i] = ratio;
		}
		return ratioArray;
	}
	
	function draw(ratioArray) {
		var i = 0;
		
		for(i; i < ratioArray.length; i++) {
			var ratio = ratioArray[i];
			var pos = elementHeight * (1 - ratio);
			drawPriceScale(yAxis.getRoundedTick(ratio), pos);
		}
		
		function drawPriceScale(price, coordPos) {
			
			var text = drawText("50%", coordPos + 4, "Arial", "12px", "middle", price);
			verticalAxis.appendChild(text);
			
			var textBox = text.getBBox();
			if(textBox.y + textBox.height >= verticalAxis.getBoundingClientRect().height ||
					textBox.y <= 0) {
				verticalAxis.removeChild(text);
				return;
			}
			
			var rect = document.createElementNS(NS, "rect");
			rect.setAttribute("x", textBox.x);
			rect.setAttribute("y", textBox.y);
			rect.setAttribute("width", textBox.width);
			rect.setAttribute("height", textBox.height);
			rect.setAttribute("fill", "white");
			
			var line = drawLine("10%", coordPos, "90%", coordPos, "#DDDDDD", "1px", "crispEdges");

			verticalAxis.insertBefore(line, text);
			verticalAxis.insertBefore(rect, text);
		}
	}
}

function drawHorizontalAxis() {
	
	var monthMinimumWidth = 100; // minimal distance between the lines at the begining and the end of a month in px;
	var monthCount = getMonthsCount(dateObject);
	processMonth(dateObject, getScale());
	
	function drawMonth(CoordX, month) {
		var line = drawLine(CoordX, "5%", CoordX, "60%", "#DDDDDD", "1px", "crispEdges");
		horizontalAxis.appendChild(line);
		
		var text = drawText(CoordX + 4, "45%", "Arial", "12px", "left", toMonthString(month));
		horizontalAxis.appendChild(text);
	}
	
	function drawYear(CoordX, year) {
		//var line = drawLine(CoordX + 4, "60%", "Arial", "12px", "left", toMonthString(month));
		//g.insertBefore(line, g.firstElementChild.nextElementSibling);
		var yearText = drawText(CoordX, "95%", "Arial", "12px", "middle", year);
		horizontalAxis.appendChild(yearText);
		
	}
	
	function getScale() {
		var monthWidth = elementWidth / monthCount;
		if(monthWidth <= monthMinimumWidth) {
			return Math.floor(monthMinimumWidth / monthWidth);
		} else {
			console.log(1);
			return 1;
		}
	}

	function getMonthsCount(arrayOfDates){ // returns the number of months in a dateArray;
		
		var monthCount = 0;
		for(var i = 0; i < arrayOfDates.length; i++) {
			var yearObjKey = Object.keys(arrayOfDates[i]); // the year;
			var yearObjValues = arrayOfDates[i][yearObjKey]; // the array of month objects;
			
			monthCount += yearObjValues.length;
		}
		return monthCount;
	}
	
	function processMonth(arrayOfDates, scale) { // returns the month number 
		var dayCount = 0;
		for(var i = 0; i < arrayOfDates.length; i++) {
			var yearObjKey = parseInt(Object.keys(arrayOfDates[i])); // the year;
			var yearObjValues = arrayOfDates[i][yearObjKey]; // the array of month objects;
			
			for(var j = 0; j < yearObjValues.length; j++) {
				var monthObj = yearObjValues[j], // month object;
					month = Object.keys(monthObj),
					monthInt = parseInt(month), // the month;
					dayArray = monthObj[month];
				
				for(var k = 0; k < dayArray.length; k++) { // itterate through days;
					dateArray[dayCount] = new Date(yearObjKey, monthInt, dayArray[k]);
					dayCount ++;
					var isOutOfArea = coord > horizAxisWidth * 0.95; // boolean that indicates, if the month is not beyond the chart drawing range;
					if(monthInt === 1 && k === 0 && !isOutOfArea) {
						var coord = horizAxisWidth / dateCount * (dayCount - 1);
						drawYear(coord, yearObjKey);
						drawMonth(coord, monthInt);
					} else if (month % scale === 0 && k === 0 && !isOutOfArea) {
						var coord = horizAxisWidth / dateCount * (dayCount - 1);
						drawMonth(coord, monthInt);
					}
				}
			}
		}
	}
}

function updateInfoBar() {
	dateInfoText.innerHTML = getDate(dayPos).toDateString() + " @ ";
	dateInfoText.setAttribute("x", 10);
	priceInfoText.innerHTML = priceText.innerHTML;
	priceInfoText.setAttribute("x", dateInfoText.getBBox().y + dateInfoText.getBBox().width + 10);
}

function getDate(index) {
	return dateArray[index];
}

function drawLine(x1, y1, x2, y2, stroke, strokeWidth, shapeRendering) {
	var line = document.createElementNS(NS, "line");
	line.setAttribute("x1", x1);
	line.setAttribute("x2", x2);
	line.setAttribute("y1", y1);
	line.setAttribute("y2", y2);
	line.setAttribute("stroke", stroke);
	line.setAttribute("stroke-width", strokeWidth);
	line.setAttribute("shape-rendering", shapeRendering);
	
	return line;
}

function drawText(x, y, fontFamily, fontSize, textAnchor, innerText) {
	var text = document.createElementNS(NS, "text");
	text.setAttribute("x", x);
	text.setAttribute("y", y);
	text.setAttribute("font-family", fontFamily);
	text.setAttribute("font-size", fontSize);
	text.setAttribute("text-anchor", textAnchor);
	text.innerHTML = innerText;
	return text;
}

function toMonthString(month) {
	var result = null;
	switch(month) {
	case 1: 
		result = "Jan";
		break;
	case 2: 
		result = "Feb";
		break;
	case 3: 
		result = "Mar";
		break;
	case 4: 
		result = "Apr";
		break;
	case 5: 
		result = "May";
		break;
	case 6: 
		result = "Jun";
		break;
	case 7: 
		result = "Jul";
		break;
	case 8: 
		result = "Aug";
		break;
	case 9: 
		result = "Sep";
		break;
	case 10: 
		result = "Oct";
		break;
	case 11: 
		result = "Nov";
		break;
	case 12: 
		result = "Dec";
		break;
	}
	return result;
}

function getTickHeight() {
	var returnVal = (elementHeight - (elementHeight * padding)) / range;
	return returnVal;
}

function round(exactPrice) {
	var result;
	result = Math.round(exactPrice / tickSize) * tickSize;
	return result;
};
