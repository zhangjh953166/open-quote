<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <head> 
      <title>${title}</title> 
      <link rel="stylesheet" href="${url.context}/css/basic.css" type="text/css"/>
      <!--${head}-->
   </head>
   <body>
      <div>
         <@region id="header" scope="global" protected=true>
            Default region content (only see this if component cannot be resolved!)
         </@region>
      </div>
      A header component is above this plain text.
      <br/>
      Now a nav component appears...
      <div>
         <@region id="nav" scope="global" protected=true />
      </div>
      Some plain text, and another component...
      <div>
         <@region id="content" scope="page" />
      </div>
   </body>
</html>