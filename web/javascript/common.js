function showDialog(title, body) {
	var div = $('<div title="' + title + '"></div>');
	div.append($('#' + body).clone().show()).dialog({
		bgiframe: true,
		modal: true,
		width: '500px',
		show: 'highlight',
		hide: 'highlight'
	});
}

function ajaxLoad(url, target) {
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			$(target).html(data);
		}
	});
}