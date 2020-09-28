/**
 * 
 */
(function ($) {
	setMemo();
	
}(jQuery));
function resizeGridItem(item){
  grid = document.getElementsByClassName("grid")[0];
  rowHeight = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-auto-rows'));
  rowGap = parseInt(window.getComputedStyle(grid).getPropertyValue('grid-row-gap'));
  rowSpan = Math.ceil((item.querySelector('.content').getBoundingClientRect().height+rowGap)/(rowHeight+rowGap));
    item.style.gridRowEnd = "span "+rowSpan;
}

function resizeAllGridItems(){
  allItems = document.getElementsByClassName("item");
  for(x=0;x<allItems.length;x++){
    resizeGridItem(allItems[x]);
  }
}

function resizeInstance(instance){
	item = instance.elements[0];
  resizeGridItem(item);
}

window.onload = resizeAllGridItems();
window.addEventListener("resize", resizeAllGridItems);

allItems = document.getElementsByClassName("item");



function setMemo(){
	$.ajax({
		type: "POST",
		url: '/memo/getMyMemo',
		success: function (data) {
			$('div.grid').html('');
			$.each(data.memos, function(idx, i){
				const type = i.type;
				const title = i.title;
				const img = i.img;
				const desc = i.desc;
				const _id = i._id;
				
				let ifImg = ``;
				if(img != null){
					ifImg = `<img class="photothumb" src="/mongoboard/${img}">`;
				}
				
				$('div.grid').append(
				 `<div class="item ${type}">
				    <div class="content">
				      <div class="title">
				        <h3>${title}</h3>
				      </div>
				      ${ifImg}
				      <div class="desc">
				        <p>${desc}</p>
				      </div>
				    </div>
				  </div>`
				)
				
			})
			window.onload = resizeAllGridItems();
			window.addEventListener("resize", resizeAllGridItems);

			allItems = document.getElementsByClassName("item");
		},
		error: function (jqXHR, textStatus, errorThrown) {
			console.log(jqXHR.status);
			console.debug(jqXHR.responseText);
			console.log(errorThrown);
		}
	})
}