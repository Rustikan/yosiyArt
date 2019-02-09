<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Йоська</title>
</head>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
    var prefix = '/yosiyArt_war';
    var RestPost = function() {
        $.ajax({
            type: 'POST',
            url:  prefix+'/post',
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Кол-во: ' +result.length);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    var RestPost2 = function() {
        var JSONObject= [
         {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
         {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
         {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
         {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"}];
        $.ajax({
            type: 'POST',
            url:  prefix+'/rs/orgs/01/invts/force',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(JSONObject),
            dataType: 'json',
            async: true,
            success: function(result) {
                alert('Кол-во: ' +result.length);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }
</script>

<body>

<h3>Это простой пример </h3>

<button type="button" onclick="RestPost()">Метод POST</button>
<button type="button" onclick="RestPost2()">Метод POST2</button>

</body>
</html>