<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<page:applyDecorator name="project">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>	
	  <title><fmt:message key="iteration"/></title>
	  <decorator:head />
	  <c:if test="${not empty iteration}">
		  <script type="text/javascript">
		  $(document).ready(function() {
		      var daysBetweenTodayAndStartDate = (parseInt('${today.dayOfYear}') + 365 * parseInt('${today.year}')) - (parseInt('${iteration.startDate.dayOfYear}') + 365 * parseInt('${iteration.startDate.year}'));
		      var daysBetweenEndDateAndToday = (parseInt('${iteration.endDate.dayOfYear}') + 365 * parseInt('${iteration.endDate.year}')) - (parseInt('${today.dayOfYear}') + 365 * parseInt('${today.year}'));
		      var daysBetweenEndDateAndStartDate = (parseInt('${iteration.endDate.dayOfYear}') + 365 * parseInt('${iteration.endDate.year}')) - (parseInt('${iteration.startDate.dayOfYear}') + 365 * parseInt('${iteration.startDate.year}'));
		
		      timeline(daysBetweenTodayAndStartDate, daysBetweenEndDateAndToday, daysBetweenEndDateAndStartDate);
		      $('#timeline').insertAfter('#page-tabs ul:first');
		  });
		    initialize('<c:url value="/projects/${iteration.project.id }/iterations/${iteration.id}/cards/"/>');
		  </script>
	  </c:if>
	</head>
	
	<body>
	
    <c:if test="${not empty iteration}">
    	<div id="timeline">
		    <%@include file="timeline.jsp" %>
		    <div id="iteration_text">
		      <p><fmt:message key="iteration.goal"/>: ${iteration.goal}</p>
		    </div>
    	</div>
	</c:if>
		<decorator:body />
	</body>
	</html>
</page:applyDecorator>