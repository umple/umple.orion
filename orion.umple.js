var provider = new orion.PluginProvider({
  name: "Orion.Umple",
  version: "alpha",
  description: "Umple (Model-Oriented Programming) support for Orion."
});

// Adds a new content type that recognizes Umple textual models
provider.registerService("orion.core.contenttype", {}, {
  contentTypes: [
    {
      id: "text/umple",
      name: "Textual Umple Model",
      extension: [
        "ump"
      ],
      // extends must be a string as it is otherwise reserved in Javascript
      "extends": "text/plain"
    }
  ]
});

// Adds a grammar for highlighting code and keywords in Umple models
provider.registerService("orion.edit.highlighter", {}, {
  id: "orion.umple",
  contentTypes: ["text/umple"],
  patterns: [
    // Matcing braces and parenthesis from Orion's library
    { include: "orion.lib#string_doubleQuote" },
    { include: "orion.lib#string_singleQuote" },
    { include: "orion.lib#brace_open" },
    { include: "orion.lib#brace_close" },
    { include: "orion.lib#bracket_open" },
    { include: "orion.lib#bracket_close" },
    { include: "orion.lib#parenthesis_open" },
    { include: "orion.lib#parenthesis_close" },

    // Numbers and c-style comments, which Umple uses.
    { include: "orion.lib#number_decimal" },
    { include: "orion.c-like#comments" },

    // Keywords
    {
      name: "keyword.other.reserved",
      match: "\\b(?:class|interface|namespace|external|generate|strictness|association|trait|depend|use|isA|lazy|immutable|const|settable|internal|autounique|defaulted|before|after|Integer|Float|String|Double|Boolean|Date|Time|->|--|entry|do|exit|singleton|trace|tracer)\\b"
    },

    // For extra code, straight import for now like in UmpleOnline.
    { include: "orion.java" },
    { include: "orion.cpp" },
    { include: "orion.php" }
  ]
});

["Java", "Php", "RTCpp", "Ruby"].forEach(function (lang){
  provider.registerService("orion.edit.command", {
    run: function (selectedText, text, selection, fileName) {
      compileUmple(text, fileName.slice(0, fileName.lastIndexOf("/")), lang);
    }
  }, {
    name: "Compile Umple Model ("+lang+")",
    id: "compile.umple."+lang.toLowerCase(),
    tooltip: "Compile this Umple Model into "+ lang +" code using UmpleOnline.",
    contentType: ["text/umple"]
  });
});

//Prototype for umple compilation.
//Files will only write if you disable web security. As such, all file writing is commented out.
var compileUmple = function (modelText, targetDirectory, targetLang) {
  compiler = new XMLHttpRequest();
  compiler.open("POST", "http://cruise.eecs.uottawa.ca/umpleonline/scripts/compiler.php", true);
  var data = new FormData();
  data.append('language', targetLang);
  data.append('languageStyle', 'java');
  data.append('error', 'true');
  data.append('umpleCode', modelText+"//$?[End_of_model]$?");
  compiler.addEventListener("load", handleResult);
  compiler.send(data);

  function handleResult () {
    var response = decodeHtml(this.responseText);
    do {
      var fileWriter = new XMLHttpRequest();
      response = response.slice(response.indexOf("//%%")+14);
      filename = response.slice(0, response.indexOf(' ')) + getExt(targetLang);
      console.log(filename);
      fileWriter.onload = function () {
        // Clear the "onload" function because we will use the same XHR for writing the contents
        fileWriter.onload = function () { return; };
        response = response.slice(response.indexOf("\n")+1);
        response = response.slice(response.indexOf("\n")+1);
        if (response.indexOf("//%%") >= 0) {
          fileContents = response.slice(0, response.indexOf("//%%"));
        } else {
          fileContents = response;
        }
        console.log(fileContents);
        //Insert contents into target file
        //fileWriter.open("PUT", "http://orionhub.org"+targetDirectory+"/"+filename);
        //fileWriter.send(fileContents);
      }
      //Create target file
      //fileWriter.open("POST", "http://orionhub.org"+targetDirectory+"/"+filename);
      //fileWriter.send();
    } while(response.indexOf("//%%") != -1);
  }

  function getExt(lang){
    switch(lang){
      case "Java" : return ".java";
      case "Php"  : return ".php";
      case "RTCpp": return "";      //UmpleOnline takes care of C++ extensions.
      case "Ruby" : return ".rb";
    }
  }

  //Turns the compiler's HTML escape codes into plaintext.
  function decodeHtml(html) {
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
  }

};

// Get connected to Orion so that services can actually be provided.
provider.connect();
