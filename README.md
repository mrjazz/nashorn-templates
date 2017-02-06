It is a small server-side HTML templating language with some server-side scripting in javascript.

You have template like this:

```html
<!DOCTYPE html>
<html>
  <scripttype="server/javascript">
    importClass(Packages.biz.test.Person) 
    var id=request.getParameter("id") 
    var person=Person.lookup(id) 
  </script>
<head>
  <title>${person.name}</title>
</head>
<body>
  <h1 title="${person.name}">${person.name}</h1>
  <h2 data-if="person.married" title="${person.spouse}">Spouse: ${person.spouse}</h2>
  <div data-for-child="person.children">Child: ${child}</div>
</body>
</html>
```

Then, a provided Person Java class:

```java
package biz.test; 
public class Person{
  public static Person lookup(String id){ 
    return ...; 
  }
  public String getName() {...} 
  public booleanisMarried() {...} 
  public String getSpouse() {...}
  public List<String>getChildren() {...} 
}
```

And it renders an HTML page like:

```html
<!DOCTYPE html>
<html>
<head>
   <title>Maria</title>
</head>
<body>
  <h1 title="Maria">Maria</h1>
  <h2 title="Pere">Spouse: Pere</h2>
  <div>Child: Anna</div>
  <div>Child: Berta</div>
  <div>Child: Clara</div>
</body>
</html>
```