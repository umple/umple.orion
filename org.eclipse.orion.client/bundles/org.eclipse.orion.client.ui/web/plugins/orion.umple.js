require(["orion/Deferred", "orion/plugin"], function(Deferred, PluginProvider){
  var provider = new PluginProvider({
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
    id: "orion.umple.highlight",
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

      // Umple keywords
      {
        name: "keyword.other.reserved",
        match: "\\b(?:class|interface|namespace|external|generate|strictness|association|trait|depend|use|isA|lazy|immutable|const|settable|internal|autounique|defaulted|before|after|Integer|Float|String|Double|Boolean|Date|Time|->|--|entry|do|exit|singleton|trace|tracer)\\b"
      },

      // For extra code, straight import for now like in UmpleOnline.
      { include: "orion.java" },
      { include: "orion.cpp" },
      { include: "orion.php" }
      // There's no ruby at this time, unfortunately.
    ]
  });

  // Registers a new compiler for each language
  // Adding a new languate to the array should fully add another language to the compiler.
  ["Java", "Php", "Cpp", "Ruby"].forEach(function(lang){
    provider.registerService("orion.edit.command", {
      run: function (selectedText, text, selection, fileName) {
        var result = compileUmple(text, fileName.slice(0, fileName.lastIndexOf("/")), lang);
        // TODO window.alert doesn't work due to the plugin being sandboxed. This will likely be patched in another version of Orion.
        if (result == null) {
          window.alert("Compilation Finished. Refresh the page to see the compiled files.");
          console.log("Compilation Finished. Refresh the page to see the compiled files.");
        } else {
          window.alert("Compilation error: " + result);
          console.log("Compilation error: " + result);
        }
      }
    }, {
      name: "Compile Umple Model ("+lang+")",
      id: "orion.umple.compile."+lang.toLowerCase(),
      tooltip: "Compile this Umple Model into "+ lang +" code using UmpleOnline.",
      contentType: ["text/umple"]
    });
  });

  // RESTFUL compiler using UmpleOnline
  function compileUmple (modelText, targetDirectory, targetLang) {
    compiler = new XMLHttpRequest();
    compiler.open("POST", "http://cruise.eecs.uottawa.ca/umpleonline/scripts/compiler.php", true);
    var data = new FormData();
    data.append('language', targetLang);
    data.append('languageStyle', 'java');
    data.append('error', 'true');
    data.append('umpleCode', modelText+"//$?[End_of_model]$?");
    compiler.addEventListener("loadend", function () {
      var compilerResponse = decodeHtml(this.responseText);
      // Return an error if the compiler hasn't sent any files.
      if (compilerResponse.indexOf("//%%") == -1) {
        return compilerResponse;
      }
      do {
        compilerResponse = compilerResponse.slice(compilerResponse.indexOf("//%%")+14);
        filename = compilerResponse.slice(0, compilerResponse.indexOf(' ')) + getExt(targetLang);
        // Create the new file.
        var fileCreate = new XMLHttpRequest();
        fileCreate.open("POST", targetDirectory);
        fileCreate.addEventListener("loadend", function () {
          // Write the file contents.
          fileWrite = new XMLHttpRequest();
          compilerResponse = compilerResponse.slice(compilerResponse.indexOf("\n")+1);
          compilerResponse = compilerResponse.slice(compilerResponse.indexOf("\n")+1);
          if (compilerResponse.indexOf("//%%") >= 0) {
            fileContents = compilerResponse.slice(0, compilerResponse.indexOf("//%%"));
          } else {
            fileContents = compilerResponse;
          }
          fileWrite.open("PUT", targetDirectory+"/"+filename);
          // Send the file contents.
          fileWrite.send(fileContents);
        });
        // Send the file creation
        fileCreate.send(filename);
      } while (compilerResponse.indexOf("//%%") != -1);
      return null;
    });
    // Send the model to UmpleOnline
    compiler.send(data);

    // Get the file extension for generated files
    function getExt(lang){
      switch (lang) {
      case "Java" : return ".java";
      case "Php"  : return ".php";
      case "Cpp"  : return "";      // UmpleOnline takes care of C++ extensions.
      case "Ruby" : return ".rb";
      default     : return "";
      }
    }

    // Turns the compiler's HTML escape codes into plaintext.
    function decodeHtml(html) {
      var txt = document.createElement("textarea");
      txt.innerHTML = html;
      return txt.value;
    }
  }

  // Get connected to Orion so that services can actually be provided.
  provider.connect();
});

