<li class="card" id="${cardId}_${s.count}" name="${card.name }" ondblclick="showDialog('${card.name}', 'description_${card.id }')">
	<p>${card.name }</p>
	<pre class="hidden" id="description_${card.id }">${fn:escapeXml(card.description) }</pre>
	<span class="hidden">${card.id }</span>
</li>