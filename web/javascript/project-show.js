function confirmIterationDeletion(url) {
    var msg = {};
    msg['deletion'] = {
        html : 'Are you sure to delete?',
        buttons : {
            'Yes' : true,
            'No' : false
        },
        submit : function(confirm) {
            if (confirm) {
                window.location.href = url;
            }
        }
    };
    $.prompt(msg);
}

function toggleDescription() {
	$('.description').slideToggle();
}
$( function() {
    $("#storyForm").validate( {
        rules : {
            "story.name" : {
                required : true,
                minlength : 4
            },
            "story.description" : {
                required : true,
                minlength : 8
            }
        },
        submitHandler : function(form) {
            $(form).ajaxSubmit( {
                target : "#stories",
                resetForm : true,
                error : function() {
                    window.location.href = window.location + '../../../';
                }
            });
            return false;
        }
    });
    $("#colaborator").validate( {
        rules : {
            "colaborator.login" : {
                required : true
            }
        }
    });
    
});