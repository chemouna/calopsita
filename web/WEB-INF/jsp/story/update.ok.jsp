<%@include file="../javascripts.jspf" %>
<script type="text/javascript">
	$(function() {
		function bind() {
			$('form[name="editStory"]').ajaxForm({
				beforeSubmit: function () {
					$('[id*="story_edit"]:visible').slideToggle("normal");
				},
				success: function(data) {
					$('#stories').html(data);
					bind();
				}
			});
		}
		bind();
	});
</script>
<h2>Stories:</h2>

<ol>
	<c:forEach items="${stories}" var="story" varStatus="s">
		<c:if test="${story.priority ne 0}">
			<%@include file="storyLine.jsp" %>
		</c:if>
	</c:forEach>
	<c:forEach items="${stories}" var="story" varStatus="s">
		<c:if test="${story.priority eq 0}">
			<%@include file="storyLine.jsp" %>
		</c:if>
	</c:forEach>
</ol>
<a href="<c:url value="/project/${project.id }/prioritization/"/>">Prioritize</a>