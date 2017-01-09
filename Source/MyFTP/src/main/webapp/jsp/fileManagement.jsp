<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>MyFTP</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/themes/default/easyui.css">
<link rel="stylesheet"
	href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link href="http://v3.bootcss.com/examples/dashboard/dashboard.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/myCss.css"
	rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
</head>
<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<span class="glyphicon glyphicon-asterisk">brand</span>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="http://v3.bootcss.com/examples/dashboard/#">Help</a></li>
			</ul>
			<form class="navbar-form navbar-right">
				<input type="text" class="form-control" placeholder="Search...">
			</form>
		</div>
	</div>
	</nav>

	<div class="container-fluid">
		<h2 class="sub-header">Wecome to use the file Management</h2>
		<!--up-->
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">file upload</h3>
			</div>
			<div class="panel-body">
				<form role="form-horizontal" id="uploadform"
					action="uploadobject.do" method="post"
					enctype="multipart/form-data">

					<div class="form-group has-info has-feedback">
						<label for="name" style="float: center">选择文件:</label> <input
							class="btn btn-info" type="file" name="uploadfile" value="上传文件"
							id="filename"></input>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-10 col-sm-10">
							<input type="button" class="btn btn-lg btn-info" value="上传"
								id="upbutton">
						</div>
					</div>

				</form>

			</div>
		</div>

		<!-- down -->
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">
					dataObject download
				</h3>
			</div>
			<div class="panel-body">
				<h3 class="panel-title col-md-3">file operations</h3>
				<input type="button"
						class="btn btn-sm btn-success" id="query" value="refresh">
				<table id="datatable" class="easyui-datagrid"
					data-options="
					        	url:'getFiles.do',
					        	fitColumns:true,
					        	singleSelect:true,
					        	title:'result',
					        	pagination:true,
					        	height:300,
					        	checkOnSelect:true,
					        	rownumbers:true,
					        	showHeader:true,
					        	showFooter:true,
					        	singleSelect:false,
					        	queryParams:{query:'false'},
					        ">
					<thead>
						<tr>
							<th data-options="field:'fileName',width:100,align:'center'">fileName</th>
							<th data-options="field:'size',width:100,align:'center'">size(B)</th>
							<th data-options="field:'modifierTime',width:100,align:'center'">modifierTime</th>
							<th data-options="field:'delete',width:100,align:'center',formatter:deleteFormater">delete</th>
							<th data-options="field:'download',width:100,align:'center',formatter:downloadFormater">download</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>


	</div>

	<script type="text/javascript">
	
		function deleteFormater(value, row, index) {
	        return "<a href='delete.do?fileName="+row.fileName+"'>delete</a>";
	    }
		
		function downloadFormater(value, row, index) {
	        return "<a href='download.do?fileName="+row.fileName+"'>download</a>";
	    }
	
		$(document).ready(function() {
			var a = '<%=request.getAttribute("result")%>';

			if (a != "null") {
				$.messager.show({
					title : '操作结果',
					msg : a,
					timeout : 5000,
					showType : 'slide'
				});
			}
			
			///////表格提交判断 
			$('#upbutton').click(function() {
				var ckey = $('#filename').val();
				if (ckey == "") {
					$.messager.alert('警告', '提交信息不能为空，请认真填写');
				} else {
					$('#uploadform').submit();
				}
			});


			
			$('#query').click(function(){
				$('#datatable').datagrid('load',{query:'true'});
			});
		});
	</script>

</body>
</html>

