/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl_v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.dom.lrparser.cpp;

public interface CPPExpressionParsersym {
    public final static int
      TK_asm = 60,
      TK_auto = 47,
      TK_bool = 14,
      TK_break = 77,
      TK_case = 78,
      TK_catch = 119,
      TK_char = 15,
      TK_class = 61,
      TK_const = 45,
      TK_const_cast = 31,
      TK_continue = 79,
      TK_default = 80,
      TK_delete = 62,
      TK_do = 81,
      TK_double = 16,
      TK_dynamic_cast = 32,
      TK_else = 122,
      TK_enum = 66,
      TK_explicit = 48,
      TK_export = 87,
      TK_extern = 17,
      TK_false = 33,
      TK_float = 18,
      TK_for = 82,
      TK_friend = 49,
      TK_goto = 83,
      TK_if = 84,
      TK_inline = 50,
      TK_int = 19,
      TK_long = 20,
      TK_mutable = 51,
      TK_namespace = 56,
      TK_new = 63,
      TK_operator = 7,
      TK_private = 114,
      TK_protected = 115,
      TK_public = 116,
      TK_register = 52,
      TK_reinterpret_cast = 34,
      TK_return = 85,
      TK_short = 21,
      TK_signed = 22,
      TK_sizeof = 35,
      TK_static = 53,
      TK_static_cast = 36,
      TK_struct = 67,
      TK_switch = 86,
      TK_template = 54,
      TK_this = 37,
      TK_throw = 57,
      TK_try = 75,
      TK_true = 38,
      TK_typedef = 55,
      TK_typeid = 39,
      TK_typename = 10,
      TK_union = 68,
      TK_unsigned = 23,
      TK_using = 58,
      TK_virtual = 44,
      TK_void = 24,
      TK_volatile = 46,
      TK_wchar_t = 25,
      TK_while = 76,
      TK_integer = 40,
      TK_floating = 41,
      TK_charconst = 42,
      TK_stringlit = 28,
      TK_identifier = 1,
      TK_Completion = 2,
      TK_EndOfCompletion = 8,
      TK_Invalid = 123,
      TK_LeftBracket = 59,
      TK_LeftParen = 3,
      TK_Dot = 120,
      TK_DotStar = 96,
      TK_Arrow = 103,
      TK_ArrowStar = 90,
      TK_PlusPlus = 26,
      TK_MinusMinus = 27,
      TK_And = 9,
      TK_Star = 6,
      TK_Plus = 11,
      TK_Minus = 12,
      TK_Tilde = 5,
      TK_Bang = 29,
      TK_Slash = 91,
      TK_Percent = 92,
      TK_RightShift = 88,
      TK_LeftShift = 89,
      TK_LT = 30,
      TK_GT = 65,
      TK_LE = 93,
      TK_GE = 94,
      TK_EQ = 97,
      TK_NE = 98,
      TK_Caret = 99,
      TK_Or = 100,
      TK_AndAnd = 101,
      TK_OrOr = 102,
      TK_Question = 117,
      TK_Colon = 73,
      TK_ColonColon = 4,
      TK_DotDotDot = 95,
      TK_Assign = 70,
      TK_StarAssign = 104,
      TK_SlashAssign = 105,
      TK_PercentAssign = 106,
      TK_PlusAssign = 107,
      TK_MinusAssign = 108,
      TK_RightShiftAssign = 109,
      TK_LeftShiftAssign = 110,
      TK_AndAssign = 111,
      TK_CaretAssign = 112,
      TK_OrAssign = 113,
      TK_Comma = 69,
      TK_RightBracket = 118,
      TK_RightParen = 74,
      TK_RightBrace = 71,
      TK_SemiColon = 13,
      TK_LeftBrace = 64,
      TK_ERROR_TOKEN = 72,
      TK_0 = 43,
      TK_EOF_TOKEN = 121;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "Completion",
                 "LeftParen",
                 "ColonColon",
                 "Tilde",
                 "Star",
                 "operator",
                 "EndOfCompletion",
                 "And",
                 "typename",
                 "Plus",
                 "Minus",
                 "SemiColon",
                 "bool",
                 "char",
                 "double",
                 "extern",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "void",
                 "wchar_t",
                 "PlusPlus",
                 "MinusMinus",
                 "stringlit",
                 "Bang",
                 "LT",
                 "const_cast",
                 "dynamic_cast",
                 "false",
                 "reinterpret_cast",
                 "sizeof",
                 "static_cast",
                 "this",
                 "true",
                 "typeid",
                 "integer",
                 "floating",
                 "charconst",
                 "0",
                 "virtual",
                 "const",
                 "volatile",
                 "auto",
                 "explicit",
                 "friend",
                 "inline",
                 "mutable",
                 "register",
                 "static",
                 "template",
                 "typedef",
                 "namespace",
                 "throw",
                 "using",
                 "LeftBracket",
                 "asm",
                 "class",
                 "delete",
                 "new",
                 "LeftBrace",
                 "GT",
                 "enum",
                 "struct",
                 "union",
                 "Comma",
                 "Assign",
                 "RightBrace",
                 "ERROR_TOKEN",
                 "Colon",
                 "RightParen",
                 "try",
                 "while",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "export",
                 "RightShift",
                 "LeftShift",
                 "ArrowStar",
                 "Slash",
                 "Percent",
                 "LE",
                 "GE",
                 "DotDotDot",
                 "DotStar",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "OrOr",
                 "Arrow",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "private",
                 "protected",
                 "public",
                 "Question",
                 "RightBracket",
                 "catch",
                 "Dot",
                 "EOF_TOKEN",
                 "else",
                 "Invalid"
             };

    public final static boolean isValidForParser = true;
}
