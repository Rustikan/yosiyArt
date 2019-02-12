<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Йоська</title>
</head>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.7.7/xlsx.core.min.js"></script>
<script type="text/javascript">
    var prefix = '/yosiyArt_war';
    var fileName;

    var RestPost = function () {
        var f1=$('#files');
        fileName = $('#files')[0].files[0];

        var reader = new FileReader();
        var name = fileName.name;
        reader.onload = function (fileName) {
            var data = fileName.target.result;

            var result;
            var workbook = XLSX.read(data, {type: 'binary'});

            var sheet_name_list = workbook.SheetNames;
            sheet_name_list.forEach(function (y) { /* iterate through sheets */
                //Convert the cell value to Json
                var roa = XLSX.utils.sheet_to_json(workbook.Sheets[y]);
                if (roa.length > 0) {
                    result = roa;
                }
            });
            $.ajax({
                type: 'POST',
                url: prefix + '/post',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(result),
                dataType: 'json',
                async: true,
                success: function (result) {
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(jqXHR.status + ' ' + jqXHR.responseText);
                }
            });
            console.log(result);
        };
        reader.readAsArrayBuffer(fileName);
    }

    function handleFile(e) {
        //Get the files from Upload control
        var files = e.target.files;
        var i, f;
        //Loop through files
        for (i = 0, f = files[i]; i != files.length; ++i) {
            var reader = new FileReader();
            var name = f.name;
            reader.onload = function (e) {
                var data = e.target.result;

                var result;
                var workbook = XLSX.read(data, {type: 'binary'});

                var sheet_name_list = workbook.SheetNames;
                sheet_name_list.forEach(function (y) { /* iterate through sheets */
                    //Convert the cell value to Json
                    var roa = XLSX.utils.sheet_to_json(workbook.Sheets[y]);
                    if (roa.length > 0) {
                        result = roa;
                    }
                });
                $.ajax({
                    type: 'POST',
                    url: prefix + '/post',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify(result),
                    dataType: 'json',
                    async: true,
                    success: function (result) {
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert(jqXHR.status + ' ' + jqXHR.responseText);
                    }
                });
                console.log(result);
            };
            reader.readAsArrayBuffer(f);
        }
    }

    var RestPost2 = function () {
        var JSONObject = [
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"},
            {'name': "1", 'surname': "2", 'middleName': "3", 'snils': "4"}];
        $.ajax({
            type: 'POST',
            url: prefix + '/rs/orgs/01/invts/force',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(JSONObject),
            dataType: 'json',
            async: true,
            success: function (result) {
                alert('Кол-во: ' + result.length);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(jqXHR.status + ' ' + jqXHR.responseText);
            }
        });
    }

    //Change event to dropdownlist
    $(document).ready(function () {
        fileName = handleFile.target;
//        $('#files').change(handleFile);
    });
</script>

<body>

<h3>Это простой пример </h3>

<button type="button" onclick="RestPost()">Метод POST</button>
<button type="button" onclick="RestPost2()">Метод POST2</button>
<input type="file" id="files" name="files"/>


</body>
</html>