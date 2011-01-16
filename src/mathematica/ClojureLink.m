(* ::Package:: *)

(************************************************************************)
(* This file was generated automatically by the Mathematica front end.  *)
(* It contains Initialization cells from a Notebook file, which         *)
(* typically will have the same name as this file except ending in      *)
(* ".nb" instead of ".m".                                               *)
(*                                                                      *)
(* This file is intended to be loaded into the Mathematica kernel using *)
(* the package loading commands Get or Needs.  Doing so is equivalent   *)
(* to using the Evaluate Initialization Cells menu command in the front *)
(* end.                                                                 *)
(*                                                                      *)
(* DO NOT EDIT THIS FILE.  This entire file is regenerated              *)
(* automatically each time the parent Notebook file is saved in the     *)
(* Mathematica front end.  Any changes you make to this file will be    *)
(* overwritten.                                                         *)
(************************************************************************)



BeginPackage["ClojureLink`","JLink`"]


SetClojureLinkAutoReplacements


ToClojureExpression


FromClojureExpression


ClojureObjectEvaluate


ClojureEvaluate


$ClojureLinkDirectory


InstallClojureLink


{\:2024,\:0589 ,\:2011 ,\:02cd ,\:204e}  


\:2024getObjectHandler


ClojureSymbol


getStdLink


Begin["`Private`"]


$ClojureLinkDirectory="/Work/ClojureLink/ClojureLink"


$JavaPath:=StringJoin@Riffle[{
ToFileName[{$InstallationDirectory,"SystemFiles","Links","JLink"},"JLink.jar"],
ToFileName[{$ClojureLinkDirectory,"lib"},"clojure-1.2.0.jar"],
ToFileName[{$ClojureLinkDirectory,"lib"},"clojure-contrib-1.2.0.jar"],
ToFileName[{$ClojureLinkDirectory},"ClojureLink-1.0.0-SNAPSHOT.jar"]
},":"]


InstallClojureLink[dir_]:={
$ClojureLinkDirectory=dir;
ReinstallJava[CommandLine -> "/Library/Java/Home/bin/java",JVMArguments->"-Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl -Xmx1000m -Xms1000m -Djava.library.path=/usr/local/lib/ -classpath \""<>$JavaPath<>"\""],
LoadJavaClass/@{"clojure.lang.Compiler","clojure.lang.RT","java.io.StringReader"},
Compiler`load[JavaNew["java.io.StringReader",
"(use 'ClojureLink.core)"]]}


$DotInput="\\";$DotSymbol="\:2024";


$DashInput="--";$DashSymbol="\:2011";


$UnderscoreInput="-=";$UnderscoreSymbol="\:02cd";


$ColonInput="''";$ColonSymbol="\:0589";


$AstriskInput="**";$AstriskSymbol="\:204e";


$QuestionInput="\[DownQuestion]";$QuestionSymbol="\:2047";


$ExclamationInput="!!";$ExclamationSymbol="\:203c";


SetClojureLinkAutoReplacements[notebookobj_]:=SetOptions[notebookobj,InputAutoReplacements->{$DotInput->$DotSymbol,$DashInput->$DashSymbol,$UnderscoreInput->$UnderscoreSymbol,$ColonInput->$ColonSymbol,$AstriskInput->$AstriskSymbol,$QuestionInput->$QuestionSymbol,$ExclamationInput->$ExclamationSymbol}]


ToClojureExpression[x_]/;JavaObjectQ[x]:=x


ToClojureExpression[x_]:=ReturnAsJavaObject[(RT`var["ClojureLink.core","to-s-expression"])@invoke[MakeJavaExpr@(x/.y_Symbol/;JavaObjectQ[y]:>\:2024[\:2024getObjectHandler[\:2024[ClojureSymbol["com.wolfram.jlink.Install"],getStdLink[]]],getObject[SymbolName[y]]] /. Times[a_,Power[b_,-1]]:>Division[a,b])]]


ClojureObjectEvaluate[x_]/;JavaObjectQ[x]:=With[{res=(RT`var["ClojureLink.core","evalm"])@invoke[x]},res]


ClojureObjectEvaluate[x_]:=x


FromClojureExpression[Null]:=Null


FromClojureExpression[x_]/;Not[JavaObjectQ[x]]:=x


FromClojureExpression[x_]:=With[{res=(RT`var["ClojureLink.core","createExpr"])@invoke[x]},Last[res]/. Rule@@@Transpose[{res[[1]],res[[2]]}]]


ClojureEvaluate[x_]:=FromClojureExpression[ClojureObjectEvaluate[ToClojureExpression[x]]]


End[]


EndPackage[]
