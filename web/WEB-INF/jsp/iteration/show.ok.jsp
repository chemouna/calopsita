<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<calopsita:page title="Iteration" bodyClass="iteration" css="/css/iteration.css">

<div id="iteration">
    <p><fmt:message key="iteration.goal"/>: ${iteration.goal}</p>
    <c:if test="${not empty iteration.startDate}">
	    <p><fmt:message key="iteration.startDate"/>: ${iteration.startDate}</p>
    </c:if>
    <c:if test="${not empty iteration.endDate}">
	    <p><fmt:message key="iteration.endDate"/>: ${iteration.endDate}</p>
    </c:if>
</div>

<script type="text/javascript">
	function prepare() {
		$('.selectable').selectable({
			filter:'li'
		});

		$(".selectable li").draggable({
			start: function(ev, ui) {
		    	$(this).is(".ui-selected") || $(".ui-selected").removeClass("ui-selected");
				$(this).addClass('ui-selected');
			},
			revert: 'invalid',
			helper: function() {
				var div = $('<div></div>');
				if ($(this).is(".ui-selected")) {
					$('.ui-selected').each(function() {
						$(div).append($(this).clone());
					});
				} else {
					$('.ui-selected').removeClass('ui-selected');
					$(this).addClass('ui-selected');
					$(div).append($(this).clone());
				}
				return div;
			}
		});
		$('.selectable li').click(function() {
			$(this).toggleClass('ui-selected');
		});

		$('#stories').droppable({
			accept: '.backlog_story',
			drop: add_stories
		});
		$('#backlog').droppable({
			accept: '.story',
			drop: remove_stories
		});
	};
	$(prepare);
	function get_params(div) {
		var params = {};
		$(div + ' .ui-selected').each(function(c, e) {
			params['stories[' + c + '].id'] = $('.hidden', e).text();
		});
		params['iteration.id']=${iteration.id};
		return params;
	}
	function modifyStories(div, logic) {
		var params = get_params(div);

		$.ajax({
			url: logic,
			data: params,
			success: function(data) {
				$('#stories ol').html($('#stories ol', data).html());
				$('#backlog ol').html($('#backlog ol', data).html());
				prepare();
			}
		});
	}
	function add_stories() {
		modifyStories('#backlog', '<c:url value="/iteration/addStories/"/>');	
	}
	function remove_stories() {
		modifyStories('#stories', '<c:url value="/iteration/removeStories/"/>');
	}
</script>
<div id="stories">
	<h2>Stories</h2>
	<ol class="selectable">
		<c:if test="${not empty iteration.stories}">
			<c:forEach items="${iteration.stories}" var="story" varStatus="s">
				<li class="story" id="stories_${s.count}" name="${story.name }">${story.name }<span class="hidden">${story.id }</span></li>
			</c:forEach>
		</c:if>
		<input id="remove-story" type="button" value="Remove" style="display: none;" onclick="remove_stories()"/>
	</ol>
</div>
<div id="backlog">
	<h2>BackLog</h2>

	<ol class="selectable">
		<c:if test="${not empty otherStories}">
			<c:forEach items="${otherStories}" var="story" varStatus="s">
				<li class="backlog_story" id="backlog_${s.count}" name="${story.name }">${story.name }<span class="hidden">${story.id }</span></li>
			</c:forEach>
		</c:if>
	</ol>
	<input id="add-story" type="button" value="Add" style="display: none;" onclick="add_stories()"/>
</div>

<a href="<c:url value="/project/show/${iteration.project.id }/"/>">Back</a>

</calopsita:page>