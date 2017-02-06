importClass(Packages.biz.test.Person);

var id = request.getParameter("id");
var person = Person.lookup(id);

