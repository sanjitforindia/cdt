/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ed Swartz (Nokia)
 *     Markus Schorn (Wind River Systems)
 *     Andrew Ferguson (Symbian)
 *     Sergey Prigogin (Google)
 *******************************************************************************/
package org.eclipse.cdt.core.parser.tests.ast2;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import junit.framework.TestSuite;

import org.eclipse.cdt.core.dom.ast.ASTSignatureUtil;
import org.eclipse.cdt.core.dom.ast.EScopeKind;
import org.eclipse.cdt.core.dom.ast.IASTArrayDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTCastExpression;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTElaboratedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionList;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerExpression;
import org.eclipse.cdt.core.dom.ast.IASTLabelStatement;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTProblemDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStandardFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IASTTypeIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.IArrayType;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
import org.eclipse.cdt.core.dom.ast.IEnumerator;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IFunctionType;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IPointerType;
import org.eclipse.cdt.core.dom.ast.IProblemBinding;
import org.eclipse.cdt.core.dom.ast.IQualifierType;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.ITypedef;
import org.eclipse.cdt.core.dom.ast.IVariable;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCastExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTConversionName;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTLinkageSpecification;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNamespaceDefinition;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTNewExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTOperatorName;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTPointerToMember;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTQualifiedName;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTSimpleTypeConstructorExpression;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTemplateDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTUsingDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPBasicType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPBinding;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPBlockScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPConstructor;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPField;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPFunction;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPFunctionType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMember;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMethod;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPNamespace;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPNamespaceAlias;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPParameter;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPPointerToMemberType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPReferenceType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPUsingDeclaration;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPVariable;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.IGPPASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.IGPPPointerToMemberType;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.IGPPPointerType;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.core.parser.util.CharArrayUtils;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTNameBase;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ClassTypeHelper;
import org.eclipse.cdt.internal.core.dom.parser.cpp.OverloadableOperator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.CPPSemantics;
import org.eclipse.cdt.internal.core.dom.parser.cpp.semantics.CPPVisitor;
import org.eclipse.cdt.internal.core.parser.ParserException;

/**
 * @author aniefer
 */
public class AST2CPPTests extends AST2BaseTest {
	
	public AST2CPPTests() {
	}
	
	public AST2CPPTests(String name) {
		super(name);
	}
	
	public static TestSuite suite() {
		return suite(AST2CPPTests.class);
	}
	
	// #define CURLOPTTYPE_OBJECTPOINT   10000
	// #define CINIT(name,type,number) CURLOPT_ ## name = CURLOPTTYPE_ ## type + number
	// typedef enum {
	// CINIT(FILE, OBJECTPOINT, 1),
	//     CINIT(URL,  OBJECTPOINT, 2)
	// } CURLoption ;
	public void testBug102825() throws Exception {
		parseAndCheckBindings(getAboveComment());
	}
	
	// class B {
	// public:
	// B( int t );
	// };
	// class A : public B {
	// public:
	// A( int t );
	// };
	// A::A( int t ) : B( t - 1 ){}
	public void testBug78883() throws Exception {
		parseAndCheckBindings(getAboveComment());
	}
	
	// enum type {A};
	// enum type a, b;
	// type c; 
	// enum type2 {A, B};
	// enum type2 d, e; 
	public void testBug77967() throws Exception {
		parseAndCheckBindings( getAboveComment() );
	}
	
	public void testBug75189() throws Exception {
		parseAndCheckBindings( "struct A{};typedef int (*F) (A*);" ); //$NON-NLS-1$
	}
	
	public void testBug75340() throws Exception {
		IASTTranslationUnit tu = parseAndCheckBindings( "void f(int i = 0, int * p = 0);"); //$NON-NLS-1$
		IASTSimpleDeclaration sd = (IASTSimpleDeclaration) tu.getDeclarations()[0];
		assertEquals( ASTSignatureUtil.getParameterSignature( sd.getDeclarators()[0] ), "(int, int *)" ); //$NON-NLS-1$
	}
	
	// #define REF_WRAP(e) class A { public: A (){ } A& foo2(e& v) { return *this; } }
	// class B
	// {
	// REF_WRAP(B);
	// B();
	// void foo();
	// };
	public void testBug79540() throws Exception {
		parseAndCheckBindings( getAboveComment() );
	}
	
	// template <class T, int someConst=0>
	// class WithTemplate {};
	// int main ()
	// {
	// WithTemplate <int, 10> normalInstance;
	// const int localConst=10;
	// WithTemplate <int, localConst> brokenInstance;
	// return 0;
	// }
	public void testBug103578() throws Exception {
		parseAndCheckBindings( getAboveComment() );
	}
	
	// int *p1; int *p2;
	// union {
	// struct {int a; int b;} A;
	// struct {int a; int b;};
	// } MyStruct;
	// void test (void) {
	// p1 = &MyStruct.A.a;
	// p2 = &MyStruct.b;
	//         MyStruct.b = 1;
	// }
	public void testBug78103() throws Exception {
		parseAndCheckBindings( getAboveComment() );
	}
	
	// signed int si(0);
	// unsigned   u(10U);
	// signed     s(-1);
	// short      sh(0);
	// long       l(0L);
	// long long  ll(0LL);
	public void testInitializeUnsigned_Bug245070() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP, true);
	}
	
	public void testBug43579() throws Exception {
		parseAndCheckBindings("class A { int m; }; \n A * a; int A::*pm; \n int f(){} \n int f(int); \n int x = f(a->*pm);"); //$NON-NLS-1$
		parseAndCheckBindings("class A { int m; }; \n A * a; int A::*pm; \n int f(){} \n int f(int); \n int x = f(a->*pm);"); //$NON-NLS-1$
	}
	
	// class A { int m(int); };
	// A a; 
	// int (A::*pm)(int) = &A::m;
	// int f(){} 
	// int f(int); 
	// int x = f((a.*pm)(5));
	public void testBug43242() throws Exception {
		parseAndCheckBindings( getAboveComment() );
	}
	
	public void testBug43241() throws Exception {
		parseAndCheckBindings( "int m(int); int (*pm)(int) = &m; int f(){} int f(int); int x = f((*pm)(5));" ); //$NON-NLS-1$
	}
	
	// int *zzz1 (char);
	// int (*zzz2) (char); 
	// int ((*zzz3)) (char); 
	// int (*(zzz4)) (char); 
	public void testBug40768() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP ); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
	}
	
	protected IASTTranslationUnit parseAndCheckBindings( String code ) throws Exception
	{
		IASTTranslationUnit tu = parse( code, ParserLanguage.CPP ); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
		return tu;
	}
	
	
	public void testBug40422() throws Exception
	{
		IASTTranslationUnit tu = parse( "class A { int y; }; int A::* x = 0;", ParserLanguage.CPP ); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
	}
	
	public void testBug86282() throws Exception {
		IASTTranslationUnit tu = parse( "void foo() { int (* f[])() = new (int (*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
	}
	
	// bool f() {
	//     int first, last;
	// if(first < 1 || last > 99 )
	// return false;
	// }
	public void testBug75858() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
	}
	
	public void testBug95424() throws Exception {
		IASTTranslationUnit tu = parse( "void f(){ traits_type::copy(__r->_M_refdata(), __buf, __i); }", ParserLanguage.CPP, true, true ); //$NON-NLS-1$
		tu = parse( "void f(){ traits_type::copy(__r->_M_refdata(), __buf, __i); }", ParserLanguage.CPP, false, true ); //$NON-NLS-1$
		IASTFunctionDefinition f = (IASTFunctionDefinition) tu.getDeclarations()[0];
		IASTCompoundStatement cs = (IASTCompoundStatement) f.getBody();
		IASTExpressionStatement es = (IASTExpressionStatement) cs.getStatements()[0];
		assertTrue( es.getExpression() instanceof IASTFunctionCallExpression );
	}
	
	// class A { } a;
	public void testSimpleClass() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		IASTCompositeTypeSpecifier compTypeSpec = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = compTypeSpec.getName();
		
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_a = dtor.getName();
		
		ICompositeType A = (ICompositeType) name_A.resolveBinding();
		IVariable a = (IVariable) name_a.resolveBinding();
		ICompositeType A_2 = (ICompositeType) a.getType();
		assertNotNull(A);
		assertNotNull(a);
		assertSame(A, A_2);
	}
	
	// class A {
	//     public:
	// int x;
	// A * next;
	// };
	// A * start;
	// void test() {
	// for(A *y = start; y->x != 0;  y = y->next) {
	// 42;
	// }
	// for(int x = 0 ; x < 10; x++ ) {
	// }
	// }
	public void testBug95411() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector nameCol = new CPPNameCollector();
		tu.accept( nameCol );
		assertNoProblemBindings( nameCol );
	}
	
	// class A; class A {};
	public void testClassForwardDecl() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		IASTElaboratedTypeSpecifier elabSpec = (IASTElaboratedTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_elab = elabSpec.getName();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[1];
		assertEquals(decl.getDeclarators().length, 0);
		IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_comp = compSpec.getName();
		
		ICompositeType A = (ICompositeType) name_elab.resolveBinding();
		ICompositeType A_2 = (ICompositeType) name_comp.resolveBinding();
		
		assertNotNull(A);
		assertSame(A, A_2);
	}
	
	// class A {};  A a;
	public void testVariable() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		IASTCompositeTypeSpecifier compType = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = compType.getName();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[1];
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_a = dtor.getName();
		IASTNamedTypeSpecifier namedSpec = (IASTNamedTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A2 = namedSpec.getName();
		
		IVariable a = (IVariable) name_a.resolveBinding();
		ICompositeType A1 = (ICompositeType) a.getType();
		ICompositeType A2 = (ICompositeType) name_A2.resolveBinding();
		ICompositeType A = (ICompositeType) name_A.resolveBinding();
		
		assertNotNull(a);
		assertNotNull(A);
		assertSame(A, A1);
		assertSame(A1, A2);
	}
	
	// class A {  int f; };
	public void testField() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		ICPPASTCompositeTypeSpecifier comp = (ICPPASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = comp.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_f = dtor.getName();
		
		ICPPClassType A = (ICPPClassType) name_A.resolveBinding();
		IField f = (IField) name_f.resolveBinding();
		
		assertNotNull(A);
		assertNotNull(f);
		assertSame(f.getScope(), A.getCompositeScope());
	}
	
	// class A { int f(); };
	public void testMethodDeclaration() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		IASTCompositeTypeSpecifier comp = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = comp.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_f = dtor.getName();
		
		ICPPClassType A = (ICPPClassType) name_A.resolveBinding();
		ICPPMethod f = (ICPPMethod) name_f.resolveBinding();
		
		assertNotNull(A);
		assertNotNull(f);
		assertSame(f.getScope(), A.getCompositeScope());
	}
	
	//  class A { void f();  };      
	//  void A::f() { }              
	public void testMethodDefinition() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		IASTCompositeTypeSpecifier comp = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = comp.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_f1 = dtor.getName();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) tu
		.getDeclarations()[1];
		IASTFunctionDeclarator fdtor = def.getDeclarator();
		ICPPASTQualifiedName name_f2 = (ICPPASTQualifiedName) fdtor.getName();
		
		ICPPClassType A = (ICPPClassType) name_A.resolveBinding();
		ICPPMethod f1 = (ICPPMethod) name_f1.resolveBinding();
		ICPPMethod f2 = (ICPPMethod) name_f2.resolveBinding();
		
		IASTName[] names = name_f2.getNames();
		assertEquals(names.length, 2);
		IASTName qn1 = names[0];
		IASTName qn2 = names[1];
		
		ICPPClassType A2 = (ICPPClassType) qn1.resolveBinding();
		ICPPMethod f3 = (ICPPMethod) qn2.resolveBinding();
		
		assertNotNull(A);
		assertNotNull(f1);
		assertSame(f1, f2);
		assertSame(f2, f3);
		assertSame(A, A2);
	}
	
	// class A { void f(); int i;    };   
	// void A::f() { i; }                 
	public void testMemberReference() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		assertEquals(decl.getDeclarators().length, 0);
		IASTCompositeTypeSpecifier comp = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A = comp.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTDeclarator dtor = decl.getDeclarators()[0];
		IASTName name_f1 = dtor.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[1];
		dtor = decl.getDeclarators()[0];
		IASTName name_i = dtor.getName();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) tu
		.getDeclarations()[1];
		IASTFunctionDeclarator fdtor = def.getDeclarator();
		ICPPASTQualifiedName name_f2 = (ICPPASTQualifiedName) fdtor.getName();
		
		IASTCompoundStatement compound = (IASTCompoundStatement) def.getBody();
		IASTExpressionStatement statement = (IASTExpressionStatement) compound
		.getStatements()[0];
		IASTIdExpression idExp = (IASTIdExpression) statement.getExpression();
		IASTName name_i2 = idExp.getName();
		
		ICPPClassType A = (ICPPClassType) name_A.resolveBinding();
		ICPPMethod f1 = (ICPPMethod) name_f1.resolveBinding();
		ICPPMethod f2 = (ICPPMethod) name_f2.resolveBinding();
		ICPPField i1 = (ICPPField) name_i.resolveBinding();
		ICPPField i2 = (ICPPField) name_i2.resolveBinding();
		
		IASTName[] names = name_f2.getNames();
		assertEquals(names.length, 2);
		IASTName qn1 = names[0];
		IASTName qn2 = names[1];
		
		ICPPClassType A2 = (ICPPClassType) qn1.resolveBinding();
		ICPPMethod f3 = (ICPPMethod) qn2.resolveBinding();
		
		assertNotNull(A);
		assertNotNull(f1);
		assertNotNull(i1);
		assertSame(f1, f2);
		assertSame(f2, f3);
		assertSame(A, A2);
		assertSame(i1, i2);
	}
	
	// class A { int i; };               
	// class B : public A { void f(); }; 
	// void B::f() { i; }                
	public void testBasicInheritance() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		ICPPASTCompositeTypeSpecifier comp = (ICPPASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		IASTName name_A1 = comp.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTName name_i1 = decl.getDeclarators()[0].getName();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[1];
		comp = (ICPPASTCompositeTypeSpecifier) decl.getDeclSpecifier();
		IASTName name_B1 = comp.getName();
		
		ICPPASTBaseSpecifier base = comp.getBaseSpecifiers()[0];
		IASTName name_A2 = base.getName();
		
		decl = (IASTSimpleDeclaration) comp.getMembers()[0];
		IASTName name_f1 = decl.getDeclarators()[0].getName();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) tu
		.getDeclarations()[2];
		ICPPASTQualifiedName name_f2 = (ICPPASTQualifiedName) def
		.getDeclarator().getName();
		IASTName name_B2 = name_f2.getNames()[0];
		IASTName name_f3 = name_f2.getNames()[1];
		
		IASTCompoundStatement compound = (IASTCompoundStatement) def.getBody();
		IASTExpressionStatement statement = (IASTExpressionStatement) compound
		.getStatements()[0];
		IASTIdExpression idExp = (IASTIdExpression) statement.getExpression();
		IASTName name_i2 = idExp.getName();
		
		ICPPField i2 = (ICPPField) name_i2.resolveBinding();
		ICPPField i1 = (ICPPField) name_i1.resolveBinding();
		
		ICPPClassType A2 = (ICPPClassType) name_A2.resolveBinding();
		ICPPClassType A1 = (ICPPClassType) name_A1.resolveBinding();
		ICPPClassType B2 = (ICPPClassType) name_B2.resolveBinding();
		ICPPClassType B1 = (ICPPClassType) name_B1.resolveBinding();
		
		ICPPMethod f3 = (ICPPMethod) name_f3.resolveBinding();
		ICPPMethod f2 = (ICPPMethod) name_f2.resolveBinding();
		ICPPMethod f1 = (ICPPMethod) name_f1.resolveBinding();
		assertNotNull(A1);
		assertNotNull(B1);
		assertNotNull(i1);
		assertNotNull(f1);
		assertSame(A1, A2);
		assertSame(B1, B2);
		assertSame(i1, i2);
		assertSame(f1, f2);
		assertSame(f2, f3);
	}
	
	// namespace A{            
	//    int a;               
	// }                       
	// namespace B{            
	//    using namespace A;   
	// }                       
	// namespace C{            
	//    using namespace A;   
	// }                       
	//                         
	// namespace BC{           
	//    using namespace B;   
	//    using namespace C;   
	// }                       
	//                         
	// void f(){               
	//    BC::a++; //ok        
	// }                       
	public void testNamespaces() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 13);
		ICPPNamespace A = (ICPPNamespace) collector.getName(0).resolveBinding();
		IVariable a = (IVariable) collector.getName(1).resolveBinding();
		ICPPNamespace B = (ICPPNamespace) collector.getName(2).resolveBinding();
		ICPPNamespace C = (ICPPNamespace) collector.getName(4).resolveBinding();
		ICPPNamespace BC = (ICPPNamespace) collector.getName(6)
		.resolveBinding();
		IFunction f = (IFunction) collector.getName(9).resolveBinding();
		assertInstances(collector, A, 3);
		assertInstances(collector, a, 3);
		assertInstances(collector, B, 2);
		assertInstances(collector, C, 2);
		assertInstances(collector, BC, 2);
		assertInstances(collector, f, 1);
	}
	
	// int A;                       
	// class A {};                  
	// void f() {                   
	//    A++;                      
	//    class A a;                
	// }                            
	public void testNameHiding() throws Exception {
	    String content= getAboveComment();
		IASTTranslationUnit tu = parse(content, ParserLanguage.CPP);
		
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 6);
		IVariable vA = (IVariable) collector.getName(0).resolveBinding();
		ICompositeType cA = (ICompositeType) collector.getName(1)
		.resolveBinding();
		IVariable a = (IVariable) collector.getName(5).resolveBinding();
		
		assertSame(a.getType(), cA);
		assertInstances(collector, vA, 2);
		assertInstances(collector, cA, 2);
		
		tu = parse(content, ParserLanguage.CPP);
		collector = new CPPNameCollector();
		tu.accept(collector);
		
		cA = (ICompositeType) collector.getName(1).resolveBinding();
		IBinding A = collector.getName(3).resolveBinding();
		vA = (IVariable) collector.getName(0).resolveBinding();
		assertSame(vA, A);
	}
	
	// class A { void f(); };            
	// class B;                          
	// void A::f() {                     
	//    B b;                           
	// }                                 
	// int B;                            
	public void testBlockTraversal() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 9);
		ICompositeType A = (ICompositeType) collector.getName(0)
		.resolveBinding();
		ICPPMethod f = (ICPPMethod) collector.getName(1).resolveBinding();
		ICompositeType B = (ICompositeType) collector.getName(2)
		.resolveBinding();
		
		IVariable b = (IVariable) collector.getName(7).resolveBinding();
		IVariable B2 = (IVariable) collector.getName(8).resolveBinding();
		assertSame(b.getType(), B);
		assertInstances(collector, A, 2);
		assertInstances(collector, f, 3);
		assertInstances(collector, B, 2);
		assertInstances(collector, b, 1);
		assertInstances(collector, B2, 1);
	}
	
	// void f( int i );                      
	// void f( char c );                     
	// void main() {                         
	//    f( 1 );		//calls f( int );     
	//    f( 'b' );                          
	// }                                     
	public void testFunctionResolution() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		IFunction f1 = (IFunction) collector.getName(0).resolveBinding();
		IFunction f2 = (IFunction) collector.getName(2).resolveBinding();
		
		assertInstances(collector, f1, 2);
		assertInstances(collector, f2, 2);
	}
	
	// typedef struct {  
	//     int x;        
	// } S;              
	// void f() {        
	//     S myS;        
	//     myS.x = 5;    
	// }                 
	public void testSimpleStruct() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		ICPPClassType anonStruct = (ICPPClassType) collector.getName(0)
		.resolveBinding();
		ICPPField x = (ICPPField) collector.getName(1).resolveBinding();
		ITypedef S = (ITypedef) collector.getName(2).resolveBinding();
		IFunction f = (IFunction) collector.getName(3).resolveBinding();
		IVariable myS = (IVariable) collector.getName(5).resolveBinding();
		
		assertInstances(collector, anonStruct, 1);
		assertInstances(collector, x, 2);
		assertInstances(collector, S, 2);
		assertInstances(collector, f, 1);
		assertInstances(collector, myS, 2);
	}
	
	// struct A;             
	// void f(){             
	//    struct A;          
	//    struct A * a;      
	// }                     
	public void testStructureTags_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		ICPPClassType A1 = (ICPPClassType) collector.getName(0)
		.resolveBinding();
		ICPPClassType A2 = (ICPPClassType) collector.getName(2)
		.resolveBinding();
		IVariable a = (IVariable) collector.getName(4).resolveBinding();
		
		assertNotNull(a);
		assertNotNull(A1);
		assertNotNull(A2);
		assertNotSame(A1, A2);
		assertInstances(collector, A1, 1);
		assertInstances(collector, A2, 2);
	}
	
	// struct A;             
	// void f(){             
	//    struct A * a;      
	// }                     
	public void testStructureTags_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		ICPPClassType A1 = (ICPPClassType) collector.getName(0)
		.resolveBinding();
		ICPPClassType A2 = (ICPPClassType) collector.getName(2)
		.resolveBinding();
		IVariable a = (IVariable) collector.getName(3).resolveBinding();
		
		assertNotNull(a);
		assertNotNull(A1);
		assertNotNull(A2);
		assertSame(A1, A2);
		assertInstances(collector, A1, 2);
	}
	
	// struct A;                
	// struct A * a;            
	// struct A { int i; };     
	// void f() {               
	//    a->i;                 
	// }                        
	public void testStructureDef() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		ICPPClassType A1 = (ICPPClassType) collector.getName(0)
		.resolveBinding();
		IVariable a = (IVariable) collector.getName(2).resolveBinding();
		ICPPField i = (ICPPField) collector.getName(4).resolveBinding();
		
		assertInstances(collector, A1, 3);
		assertInstances(collector, a, 2);
		assertInstances(collector, i, 2);
	}
	
	// struct x {};        
	// void f( int x ) {   
	//    struct x i;      
	// }                   
	public void testStructureNamespace() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		ICPPClassType x = (ICPPClassType) collector.getName(0).resolveBinding();
		
		assertInstances(collector, x, 2);
	}
	
	// void f( int a );        
	// void f( int b ){        
	//    b;                   
	// }                       
	public void testFunctionDef() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IFunction f = (IFunction) collector.getName(0).resolveBinding();
		IParameter a = (IParameter) collector.getName(1).resolveBinding();
		
		assertInstances(collector, f, 2);
		assertInstances(collector, a, 3);
		
		IScope scope = a.getScope();
		assertNotNull(scope);
		assertSame(scope.getParent(), f.getScope());
	}
	
	// void f();              
	// void g() {             
	//    f();                
	// }                      
	// void f(){ }            
	public void testSimpleFunctionCall() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IFunction f = (IFunction) collector.getName(0).resolveBinding();
		IFunction g = (IFunction) collector.getName(1).resolveBinding();
		
		assertInstances(collector, f, 3);
		assertInstances(collector, g, 1);
	}
	
	// void f() {                         
	//    for( int i = 0; i < 5; i++ ) {  
	//       i;                           
	//    }                               
	// }                                  
	public void testForLoop() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IVariable i = (IVariable) collector.getName(1).resolveBinding();
		
		assertInstances(collector, i, 4);
	}
	
	// struct A { int x; };    
	// void f(){               
	//    ((struct A *) 1)->x; 
	// }                       
	public void testExpressionFieldReference() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		ICPPClassType A = (ICPPClassType) collector.getName(0).resolveBinding();
		IField x = (IField) collector.getName(1).resolveBinding();
		
		assertInstances(collector, A, 2);
		assertInstances(collector, x, 2);
	}
	
	// enum hue { red, blue, green };     
	// enum hue col, *cp;                 
	// void f() {                         
	//    col = blue;                     
	//    cp = &col;                      
	//    if( *cp != red )                
	//       return;                      
	// }                                  
	public void testEnumerations() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IEnumeration hue = (IEnumeration) collector.getName(0).resolveBinding();
		IEnumerator red = (IEnumerator) collector.getName(1).resolveBinding();
		IEnumerator blue = (IEnumerator) collector.getName(2).resolveBinding();
		IEnumerator green = (IEnumerator) collector.getName(3).resolveBinding();
		IVariable col = (IVariable) collector.getName(5).resolveBinding();
		IVariable cp = (IVariable) collector.getName(6).resolveBinding();
		
		assertInstances(collector, hue, 2);
		assertInstances(collector, red, 2);
		assertInstances(collector, blue, 2);
		assertInstances(collector, green, 1);
		assertInstances(collector, col, 3);
		assertInstances(collector, cp, 3);
		
		assertTrue(cp.getType() instanceof IPointerType);
		IPointerType pt = (IPointerType) cp.getType();
		assertSame(pt.getType(), hue);
	}
	
	public void testPointerToFunction() throws Exception {
		IASTTranslationUnit tu = parse("int (*pfi)();", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		IVariable pf = (IVariable) collector.getName(0).resolveBinding();
		IPointerType pt = (IPointerType) pf.getType();
		assertTrue(pt.getType() instanceof IFunctionType);
		
		tu = parse(
				"struct A; int (*pfi)( int, struct A * );", ParserLanguage.CPP); //$NON-NLS-1$
		collector = new CPPNameCollector();
		tu.accept(collector);
		ICPPClassType A = (ICPPClassType) collector.getName(0).resolveBinding();
		pf = (IVariable) collector.getName(1).resolveBinding();
		pt = (IPointerType) pf.getType();
		assertTrue(pt.getType() instanceof IFunctionType);
		IFunctionType ft = (IFunctionType) pt.getType();
		IType[] params = ft.getParameterTypes();
		assertTrue(params[0] instanceof IBasicType);
		assertTrue(params[1] instanceof IPointerType);
		pt = (IPointerType) params[1];
		assertSame(pt.getType(), A);
	}
	
	// struct A;                           
	// int * f( int i, char c );           
	// void ( *g ) ( A * );         
	// void (* (*h)(A**) ) ( int ); 
	public void testFunctionTypes() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		IASTElaboratedTypeSpecifier elabSpec = (IASTElaboratedTypeSpecifier) decl
		.getDeclSpecifier();
		ICompositeType A = (ICompositeType) elabSpec.getName().resolveBinding();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[1];
		IFunction f = (IFunction) decl.getDeclarators()[0].getName()
		.resolveBinding();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[2];
		IVariable g = (IVariable) decl.getDeclarators()[0]
		                                                .getNestedDeclarator().getName().resolveBinding();
		
		decl = (IASTSimpleDeclaration) tu.getDeclarations()[3];
		IVariable h = (IVariable) decl.getDeclarators()[0]
		                                                .getNestedDeclarator().getNestedDeclarator().getName()
		                                                .resolveBinding();
		
		IFunctionType t_f = f.getType();
		IType t_f_return = t_f.getReturnType();
		assertTrue(t_f_return instanceof IPointerType);
		assertTrue(((IPointerType) t_f_return).getType() instanceof IBasicType);
		IType[] t_f_params = t_f.getParameterTypes();
		assertEquals(t_f_params.length, 2);
		assertTrue(t_f_params[0] instanceof IBasicType);
		assertTrue(t_f_params[1] instanceof IBasicType);
		
		// g is a pointer to a function that returns void and has 1 parameter
		// struct A *
		IType t_g = g.getType();
		assertTrue(t_g instanceof IPointerType);
		assertTrue(((IPointerType) t_g).getType() instanceof IFunctionType);
		IFunctionType t_g_func = (IFunctionType) ((IPointerType) t_g).getType();
		IType t_g_func_return = t_g_func.getReturnType();
		assertTrue(t_g_func_return instanceof IBasicType);
		IType[] t_g_func_params = t_g_func.getParameterTypes();
		assertEquals(t_g_func_params.length, 1);
		IType t_g_func_p1 = t_g_func_params[0];
		assertTrue(t_g_func_p1 instanceof IPointerType);
		assertSame(((IPointerType) t_g_func_p1).getType(), A);
		
		// h is a pointer to a function that returns a pointer to a function
		// the returned pointer to function returns void and takes 1 parameter
		// int
		// the *h function takes 1 parameter struct A**
		IType t_h = h.getType();
		assertTrue(t_h instanceof IPointerType);
		assertTrue(((IPointerType) t_h).getType() instanceof IFunctionType);
		IFunctionType t_h_func = (IFunctionType) ((IPointerType) t_h).getType();
		IType t_h_func_return = t_h_func.getReturnType();
		IType[] t_h_func_params = t_h_func.getParameterTypes();
		assertEquals(t_h_func_params.length, 1);
		IType t_h_func_p1 = t_h_func_params[0];
		assertTrue(t_h_func_p1 instanceof IPointerType);
		assertTrue(((IPointerType) t_h_func_p1).getType() instanceof IPointerType);
		assertSame(((IPointerType) ((IPointerType) t_h_func_p1).getType())
				.getType(), A);
		
		assertTrue(t_h_func_return instanceof IPointerType);
		IFunctionType h_return = (IFunctionType) ((IPointerType) t_h_func_return)
		.getType();
		IType h_r = h_return.getReturnType();
		IType[] h_ps = h_return.getParameterTypes();
		assertTrue(h_r instanceof IBasicType);
		assertEquals(h_ps.length, 1);
		assertTrue(h_ps[0] instanceof IBasicType);
	}
	
	public void testFnReturningPtrToFn() throws Exception {
		IASTTranslationUnit tu = parse(
				"void ( * f( int ) )(){}", ParserLanguage.CPP); //$NON-NLS-1$
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) tu
		.getDeclarations()[0];
		IFunction f = (IFunction) def.getDeclarator().getName().resolveBinding();
		
		IFunctionType ft = f.getType();
		assertTrue(ft.getReturnType() instanceof IPointerType);
		assertTrue(((IPointerType) ft.getReturnType()).getType() instanceof IFunctionType);
		assertEquals(ft.getParameterTypes().length, 1);
	}
	
	// void f();                  
	// namespace A {              
	//    void g();               
	// }                          
	// namespace X {              
	//    using ::f;              
	//    using A::g;             
	// }                          
	// void h() {                 
	//    X::f();                 
	//    X::g();                 
	// }                          
	public void testUsingDeclaration_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IFunction f = (IFunction) collector.getName(0).resolveBinding();
		ICPPNamespace A = (ICPPNamespace) collector.getName(1).resolveBinding();
		IFunction g = (IFunction) collector.getName(2).resolveBinding();
		ICPPNamespace X = (ICPPNamespace) collector.getName(3).resolveBinding();
		
		ICPPUsingDeclaration using = (ICPPUsingDeclaration) collector
		.getName(5).resolveBinding();
		IBinding[] delegates = using.getDelegates();
		assertEquals(delegates.length, 1);
		assertSame(delegates[0], f);
		assertInstances(collector, delegates[0], 3); // decl + using-decl + ref
		assertInstances(collector, A, 2);
		assertInstances(collector, X, 3);
		
		ICPPUsingDeclaration using_g = (ICPPUsingDeclaration) collector
		.getName(8).resolveBinding();
		assertSame(using_g.getDelegates()[0], g);
		assertInstances(collector, using_g.getDelegates()[0], 3); // decl + using-decl + ref
	}
	
	// typedef int Int;      
	// void f( int i );      
	// void f( const int );  
	// void f( Int i );      
	// void g( char * );     
	// void g( char [] );    
	// void h( int(a)() );    
	// void h( int (*) () ); 
	public void testFunctionDeclarations() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IFunction f = (IFunction) collector.getName(1).resolveBinding();
		isTypeEqual(f.getType(), "void (int)");
		IFunction g = (IFunction) collector.getName(8).resolveBinding();
		isTypeEqual(g.getType(), "void (char *)");
		IFunction h = (IFunction) collector.getName(12).resolveBinding();
		isTypeEqual(h.getType(), "void (int () *)");
		
		assertInstances(collector, f, 3);
		assertInstances(collector, g, 2);
		assertInstances(collector, h, 2);
	}
	
	// class P1 { public: int x; };        
	// class P2 { public: int x; };        
	// class B : public P1, public P2 {};  
	// void main() {                       
	//    B * b = new B();                 
	//    b->x;                            
	// }                                   
	public void testProblem_AmbiguousInParent() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		IProblemBinding x = (IProblemBinding) collector.getName(12)
		.resolveBinding();
		assertEquals(x.getID(), IProblemBinding.SEMANTIC_AMBIGUOUS_LOOKUP);
	}
	
	// class D { public: int x; };       
	// class C : public virtual D {};    
	// class B : public virtual D {};    
	// class A : public B, public C {};  
	// void main() {                     
	//    A * a = new A();               
	//    a->x;                          
	// }                                 
	public void testVirtualParentLookup() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 15);
		
		ICPPClassType D = (ICPPClassType) collector.getName(0).resolveBinding();
		ICPPField x = (ICPPField) collector.getName(1).resolveBinding();
		ICPPClassType C = (ICPPClassType) collector.getName(2).resolveBinding();
		ICPPClassType B = (ICPPClassType) collector.getName(4).resolveBinding();
		ICPPClassType A = (ICPPClassType) collector.getName(6).resolveBinding();
		ICPPConstructor ctor = A.getConstructors()[0];
		assertInstances(collector, D, 3);
		assertInstances(collector, C, 2);
		assertInstances(collector, B, 2);
		assertInstances(collector, A, 2);
		assertInstances(collector, ctor, 1);
		assertInstances(collector, x, 2);
	}
	
	// class D { public: int x; };        
	// class C : public D {};             
	// class B : public D {};             
	// class A : public B, public C {};   
	// void main() {                      
	//    A * a = new A();                
	//    a->x;                           
	// }                                  
	public void testAmbiguousVirtualParentLookup() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 15);
		
		ICPPClassType D = (ICPPClassType) collector.getName(0).resolveBinding();
		ICPPField x1 = (ICPPField) collector.getName(1).resolveBinding();
		ICPPClassType C = (ICPPClassType) collector.getName(2).resolveBinding();
		ICPPClassType B = (ICPPClassType) collector.getName(4).resolveBinding();
		ICPPClassType A = (ICPPClassType) collector.getName(6).resolveBinding();
		ICPPConstructor ctor = A.getConstructors()[0];
		IProblemBinding x2 = (IProblemBinding) collector.getName(14)
		.resolveBinding();
		assertEquals(x2.getID(), IProblemBinding.SEMANTIC_AMBIGUOUS_LOOKUP);
		
		assertInstances(collector, D, 3);
		assertInstances(collector, C, 2);
		assertInstances(collector, B, 2);
		assertInstances(collector, A, 2);
		assertInstances(collector, ctor, 1);
		assertInstances(collector, x1, 1);
	}
	
	// namespace A {                      
	//    int x;                          
	// }                                  
	// int x;                             
	// namespace A {                      
	//    void f() { x; }                 
	// }                                  
	public void testExtendedNamespaces() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector collector = new CPPNameCollector();
		tu.accept(collector);
		
		assertEquals(collector.size(), 6);
		
		ICPPNamespace A = (ICPPNamespace) collector.getName(0).resolveBinding();
		IVariable x1 = (IVariable) collector.getName(1).resolveBinding();
		IVariable x2 = (IVariable) collector.getName(2).resolveBinding();
		
		assertInstances(collector, A, 2);
		assertInstances(collector, x1, 2);
		assertInstances(collector, x2, 1);
	}
	
	// class A { };
	public void testImplicitConstructors() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
		ICPPConstructor[] ctors = A.getConstructors();
		
		assertNotNull(ctors);
		assertEquals(ctors.length, 2);
		
		assertEquals(ctors[0].getParameters().length, 1);
		
		IType t = ctors[0].getParameters()[0].getType();
		assertTrue(t instanceof IBasicType);
		assertEquals(((IBasicType) t).getType(), IBasicType.t_void);
		
		assertEquals(ctors[1].getParameters().length, 1);
		
		t = ctors[1].getParameters()[0].getType();
		assertTrue(t instanceof ICPPReferenceType);
		assertTrue(((ICPPReferenceType) t).getType() instanceof IQualifierType);
		IQualifierType qt = (IQualifierType) ((ICPPReferenceType) t).getType();
		assertTrue(qt.isConst());
		assertSame(qt.getType(), A);
	}
	
	// class A { A(void);  A( const A & ); };
	public void testConstructors() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl
		.getDeclSpecifier();
		ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
		ICPPConstructor[] ctors = A.getConstructors();
		
		assertNotNull(ctors);
		assertEquals(ctors.length, 2);
		
		assertEquals(ctors[0].getParameters().length, 1);
		assertEquals(ctors[1].getParameters().length, 1);
		
		IType t = ctors[1].getParameters()[0].getType();
		assertTrue(t instanceof ICPPReferenceType);
		assertTrue(((ICPPReferenceType) t).getType() instanceof IQualifierType);
		IQualifierType qt = (IQualifierType) ((ICPPReferenceType) t).getType();
		assertTrue(qt.isConst());
		assertSame(qt.getType(), A);
	}
	
	// class A {~A(); };
	// class B {~B(void); };
	public void testExplicitDestructor_183160() throws Exception {
		// class F {(~)F(); };
		// class G {(~)G(void); };
		
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			
			ICPPMethod[] methods = ((ICPPClassScope)A.getCompositeScope()).getImplicitMethods();
			assertNotNull(methods);
			int count=0;
			for (ICPPMethod method : methods)
				count+= method.getName().startsWith("~") ? 1 : 0;
			assertEquals(line, 0, count);
			
			methods = A.getDeclaredMethods();
			assertNotNull(methods);
			count=0;
			for (ICPPMethod method : methods)
				count+= method.getName().startsWith("~") ? 1 : 0;
			assertEquals(line, 1, count);
		}
	}
	
	// class C {};
	// class D {D();};
	// class E {E(void);};
	public void testImplicitDestructor_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			
			ICPPMethod[] methods = ((ICPPClassScope)A.getCompositeScope()).getImplicitMethods();
			assertNotNull(methods);
			int count=0;
			for (ICPPMethod method : methods)
				count+= method.getName().startsWith("~") ? 1 : 0;
			assertEquals(line, 1, count);
			
			methods = A.getDeclaredMethods();
			assertNotNull(methods);
			count=0;
			for (ICPPMethod method : methods)
				count+= method.getName().startsWith("~") ? 1 : 0;
			assertEquals(line, 0, count);
		}
	}
	
	//  class A {public: A();};
	//	class B {public: B() {}};
	//	class C {public: C() {}};
	//	class D {public: D(void) {}};
	//	class E {protected: E();};
	//	class F {protected: F() {}};
	//	class G {protected: G() {}};
	//	class H {protected: H(void) {}};
	//	class I {private: I();};
	//	class J {private: J() {}};
	//	class K {private: K() {}};
	//	class L {private: L(void) {}};
	//	class M {M();};
	//	class N {N() {}};
	//	class O {O() {}};
	//	class P {P(void) {}};
	//  class Q {public: Q(int k=5);};
	//  class R {public: R(int k=5, long k=4);};
	//  class S {public: S(int k=5, int* ip= 0);};
	//  class T {public: T(int k=5, int* ip= 0, T* t= 0);};
	public void testExplicitDefaultConstructor_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPConstructor[] ctors = A.getConstructors();
			
			assertNotNull(ctors);
			assertEquals(2, ctors.length); // one user-declared default constructor, one implicit copy constructor
		}
	}
	
	//  class Q {public: Q(int k);};
	//  class R {public: R(int k=5, long k);};
	//  class S {public: S(int k=5, int* ip);};
	//  class T {public: T(int k, int* ip= 0, T* t= 0);};
	public void testExplicitNonDefaultConstructor_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPConstructor[] ctors = A.getConstructors();
			
			assertNotNull(ctors);
			assertEquals(2, ctors.length); // one user-declared non-default constructor, one implicit copy constructor
		}
	}
	
	//  class A {public: A(A &);};
	//	class B {private: B(const B &);};
	//	class C {protected: C(volatile C &);};
	//	class D {D(const volatile D &) {}};
	//	class E {public: E(E &, int k=5);};
	//	class F {private: F(const F &, int j=2, int k=3);};
	//	class G {protected: G(volatile G &, int i=4, int l=2);};
	//	class H {H(const volatile H &, int i=1, long k=2) {}};
	public void testExplicitCopyConstructor_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPConstructor[] ctors = A.getConstructors();
			
			assertNotNull(ctors);
			// one copy constructor, no implicit default constructor
			assertEquals(1, ctors.length); 
		}
	}
	
	//	class I {public: I(I *, int k=5);}; // I * rather than I &
	//	class J {private: J(const J *, int j, int k=3);}; // J * rather than J &
	//	class K {protected: K(volatile K *, int i=4, int l=2);}; // K * rather than K  &
	//	class L {L(const volatile L &, int i=1, long k=2, int* x) {}}; // param int* x has no initializer
	public void testNotExplicitCopyConstructor_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPConstructor[] ctors = A.getConstructors();
			
			assertNotNull(ctors);
			// one copy constructor, one user declared constructor (not a copy constructor)
			assertEquals(2, ctors.length); 
		}
	}
	
	//	class A {public: void operator=(int); };  // legitimate, but not a copy assignment operator
	//	class B {public: void operator=(B &, int); };  // compile error
	//	class C {public: void operator=(C &c, int k=5) {} };  // compile error
	//	class D {public: void operator=(const D &, const D &); };  // compile error
	public void testNotExplicitCopyAssignmentOperator_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPMethod[] methods = ((ICPPClassScope)A.getCompositeScope()).getImplicitMethods();
			assertNotNull(methods);
			int count=0;
			for (ICPPMethod method : methods) {
				boolean eq= Arrays.equals(method.getName().toCharArray(), OverloadableOperator.ASSIGN.toCharArray());
				count+= eq ? 1 : 0;
			}
			
			assertEquals(1, count); // check for one implicit operator= method
			
			methods = A.getDeclaredMethods();
			assertNotNull(methods);
			count=0;
			for (ICPPMethod method : methods) {
				boolean eq= Arrays.equals(method.getName().toCharArray(), OverloadableOperator.ASSIGN.toCharArray());
				count+= eq ? 1 : 0;
			}
			
			assertEquals(1, count); // check for the user declared
		}
	}
	
	//	class A {public: void operator=(A &); };
	//	class B {protected: void operator=(const B &); };
	//	class C {private: void operator=(volatile C &) {} };
	//	class D {D& operator=(volatile const D &); };  
	public void testExplicitCopyAssignmentOperator_183160() throws Exception {
		BufferedReader br= new BufferedReader(new StringReader(getAboveComment()));
		for(String line= br.readLine(); line!=null; line= br.readLine()) {
			IASTTranslationUnit tu = parse(line, ParserLanguage.CPP);
			IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu.getDeclarations()[0];
			IASTCompositeTypeSpecifier compSpec = (IASTCompositeTypeSpecifier) decl.getDeclSpecifier();
			ICPPClassType A = (ICPPClassType) compSpec.getName().resolveBinding();
			ICPPMethod[] methods = ((ICPPClassScope)A.getCompositeScope()).getImplicitMethods();
			assertNotNull(methods);
			int count=0;
			for (ICPPMethod method : methods) {
				boolean eq= Arrays.equals(method.getName().toCharArray(), OverloadableOperator.ASSIGN.toCharArray());
				count+= eq ? 1 : 0;
			}
			
			assertEquals(0, count); // check for no implicit operator= methods
			
			methods = A.getDeclaredMethods();
			assertNotNull(methods);
			count=0;
			for (ICPPMethod method : methods) {
				boolean eq= Arrays.equals(method.getName().toCharArray(), OverloadableOperator.ASSIGN.toCharArray());
				count+= eq ? 1 : 0;
			}
			
			assertEquals(1, count); // only should get the user declared
		}
	}
	
	// namespace A { int x; }   
	// namespace B = A;         
	// int f(){ B::x;  }        
	public void testNamespaceAlias() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 8);
		ICPPNamespace A = (ICPPNamespace) col.getName(0).resolveBinding();
		IVariable x = (IVariable) col.getName(1).resolveBinding();
		ICPPNamespace B = (ICPPNamespace) col.getName(2).resolveBinding();
		assertTrue(B instanceof ICPPNamespaceAlias);
		assertSame(((ICPPNamespaceAlias) B).getBinding(), A);
		
		assertInstances(col, A, 2);
		assertInstances(col, B, 2);
		assertInstances(col, x, 3);
	}
	
	// public void testBug84250() throws Exception {
	// assertTrue(((IASTDeclarationStatement) ((IASTCompoundStatement)
	// ((IASTFunctionDefinition) parse(
	// "void f() { int (*p) [2]; }",
	// ParserLanguage.CPP).getDeclarations()[0]).getBody()).getStatements()[0]).getDeclaration()
	// instanceof IASTSimpleDeclaration); //$NON-NLS-1$
	// }
	
	// void f() {                 
	//    int ( *p ) [2];         
	//    (&p)[0] = 1;            
	// }                          
	public void testBug84250() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 3);
		IVariable p = (IVariable) col.getName(1).resolveBinding();
		assertTrue(p.getType() instanceof IPointerType);
		assertTrue(((IPointerType) p.getType()).getType() instanceof IArrayType);
		IArrayType at = (IArrayType) ((IPointerType) p.getType()).getType();
		assertTrue(at.getType() instanceof IBasicType);
		
		assertInstances(col, p, 2);
	}
	
	// void f() {                 
	//    int ( *p ) [2];         
	//    (&p)[0] = 1;            
	// }                          
	public void testBug84250_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 3);
		
		IVariable p_ref = (IVariable) col.getName(2).resolveBinding();
		IVariable p_decl = (IVariable) col.getName(1).resolveBinding();
		
		assertSame(p_ref, p_decl);
	}
	
	// struct s { double i; } f(void);  
	// struct s f(void){}               
	public void testBug84266() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 7);
		
		ICompositeType s_ref = (ICompositeType) col.getName(4).resolveBinding();
		ICompositeType s_decl = (ICompositeType) col.getName(0)
		.resolveBinding();
		
		assertSame(s_ref, s_decl);
	}
	
	public void testBug84266_2() throws Exception {
		IASTTranslationUnit tu = parse("struct s f(void);", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 3);
		
		ICompositeType s = (ICompositeType) col.getName(0).resolveBinding();
		assertNotNull(s);
		
		tu = parse("struct s f(void){}", ParserLanguage.CPP); //$NON-NLS-1$
		col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 3);
		
		s = (ICompositeType) col.getName(0).resolveBinding();
		assertNotNull(s);
	}
	
	// void f( int m, int c[m][m] );        
	// void f( int m, int c[m][m] ){        
	//    int x;                            
	//    { int x = x; }                    
	// }                                    
	public void testBug84228() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 13);
		
		IParameter m = (IParameter) col.getName(3).resolveBinding();
		IVariable x3 = (IVariable) col.getName(12).resolveBinding();
		IVariable x2 = (IVariable) col.getName(11).resolveBinding();
		IVariable x1 = (IVariable) col.getName(10).resolveBinding();
		
		assertSame(x2, x3);
		assertNotSame(x1, x2);
		
		assertInstances(col, m, 6);
		assertInstances(col, x1, 1);
		assertInstances(col, x2, 2);
	}
	
	// class A { public : static int n; };  
	// int main() {                         
	//    int A;                            
	//    A::n = 42;                        
	//    A b;                              
	// }                                    
	public void testBug84615() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 9);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPField n = (ICPPField) col.getName(1).resolveBinding();
		IBinding Aref = col.getName(5).resolveBinding();
		IBinding nref = col.getName(6).resolveBinding();
		IProblemBinding prob = (IProblemBinding) col.getName(7)
		.resolveBinding();
		
		assertSame(A, Aref);
		assertSame(n, nref);
		assertNotNull(prob);
	}
	
	public void testBug84371() throws Exception {
		String code = "int x = ::ABC::DEF::ghi;"; //$NON-NLS-1$
		IASTTranslationUnit tu = parse(code, ParserLanguage.CPP);
		IASTSimpleDeclaration x = (IASTSimpleDeclaration) tu.getDeclarations()[0];
		IASTInitializerExpression e = (IASTInitializerExpression) x
		.getDeclarators()[0].getInitializer();
		IASTIdExpression id = (IASTIdExpression) e.getExpression();
		ICPPASTQualifiedName name = (ICPPASTQualifiedName) id.getName();
		assertTrue(name.isFullyQualified());
		assertEquals(name.getNames().length, 3);
		assertEquals(name.getNames()[0].toString(), "ABC"); //$NON-NLS-1$
		assertEquals(name.getNames()[1].toString(), "DEF"); //$NON-NLS-1$
		assertEquals(name.getNames()[2].toString(), "ghi"); //$NON-NLS-1$
	}
	
	// namespace Y { void f(float); }                         
	// namespace A { using namespace Y; void f(int); }             
	// namespace B { void f(char);  }                         
	// namespace AB { using namespace A; using namespace B; } 
	// void h(){         
	//    AB::f(1);      
	//    AB::f(`c`);    
	// }                 
	public void testBug84679() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP,
				false, false);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPNamespace Y = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPNamespace A = (ICPPNamespace) col.getName(3).resolveBinding();
		ICPPNamespace B = (ICPPNamespace) col.getName(7).resolveBinding();
		ICPPNamespace AB = (ICPPNamespace) col.getName(10).resolveBinding();
		
		IFunction f = (IFunction) col.getName(16).resolveBinding();
		IFunction fdef = (IFunction) col.getName(5).resolveBinding();
		assertTrue(col.getName(19).resolveBinding() instanceof IProblemBinding);
		assertSame(f, fdef);
		// assertEquals(IProblemBinding.SEMANTIC_NAME_NOT_FOUND, f2.getID());
		assertInstances(col, Y, 2);
		assertInstances(col, A, 2);
		assertInstances(col, B, 2);
		assertInstances(col, AB, 3);
	}
	
	// struct Node {          
	//    struct Node* Next;  
	//    struct Data* Data;  
	// };                     
	// struct Data {          
	//    struct Node * node; 
	//    friend struct Glob; 
	// };                     
	public void testBug84692() throws Exception {
		// also tests bug 234042.
		CPPASTNameBase.sAllowRecursionBindings= false;
		
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);

		assertEquals(col.size(), 9);

		ICPPClassType Node = (ICPPClassType) col.getName(1).resolveBinding();
		ICPPClassType Data = (ICPPClassType) col.getName(3).resolveBinding();
		assertSame(Data.getScope(), tu.getScope());

		assertInstances(col, Node, 3);
		assertInstances(col, Data, 2);
	}
	
	// namespace B { int b; }                        
	// namespace A { using namespace B;  int a;  }   
	// namespace B { using namespace A; }            
	// void f() { B::a++;  }                         
	public void testBug84686() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 11);
		
		ICPPVariable a1 = (ICPPVariable) col.getName(4).resolveBinding();
		IVariable a2 = (IVariable) col.getName(10).resolveBinding();
		assertSame(a1, a2);
	}
	
	// struct C {                               
	//    void f();                             
	//    const C& operator=( const C& );       
	// };                                       
	// const C& C::operator=( const C& other) { 
	//    if( this != &other ) {                
	//       this->~C();                        
	//       new (this) C(other );              
	//       f();                               
	//    }                                     
	//    return *this;                         
	// }                                        
	public void testBug84705() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 17);
		
		ICPPMethod f = (ICPPMethod) col.getName(1).resolveBinding();
		IASTName[] refs = tu.getReferences(f);
		assertEquals(1, refs.length);
		assertSame(f, refs[0].resolveBinding());
		
		ICPPClassType C = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPMethod op = (ICPPMethod) col.getName(3).resolveBinding();
		IParameter other = (IParameter) col.getName(11).resolveBinding();
		ICPPMethod dtor = (ICPPMethod) col.getName(13).resolveBinding();
		assertNotNull(dtor);
		assertEquals(dtor.getName(), "~C"); //$NON-NLS-1$
		assertInstances(col, C, 6);
		
		assertInstances(col, op, 3);
		assertInstances(col, other, 4);
	}
	
	// class A { void f(); void g() const; };   
	// void A::f(){ this; }                     
	// void A::g() const { *this; }             
	public void testThis() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		ICPPClassType A = (ICPPClassType) ((IASTCompositeTypeSpecifier) decl
				.getDeclSpecifier()).getName().resolveBinding();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) tu
		.getDeclarations()[1];
		IASTExpressionStatement expStatement = (IASTExpressionStatement) ((IASTCompoundStatement) def
				.getBody()).getStatements()[0];
		assertTrue(expStatement.getExpression() instanceof IASTLiteralExpression);
		IType type = expStatement.getExpression().getExpressionType();
		
		assertTrue(type instanceof IPointerType);
		assertSame(((IPointerType) type).getType(), A);
		
		def = (IASTFunctionDefinition) tu.getDeclarations()[2];
		expStatement = (IASTExpressionStatement) ((IASTCompoundStatement) def
				.getBody()).getStatements()[0];
		IASTUnaryExpression ue = (IASTUnaryExpression) expStatement.getExpression();
		type = ue.getExpressionType();
		
		assertTrue(type instanceof IQualifierType);
		assertSame(((IQualifierType) type).getType(), A);
		assertTrue(((IQualifierType) type).isConst());
	}
	
	public void testBug84710() throws Exception {
		IASTTranslationUnit tu = parse("class T { T(); };", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		ICPPConstructor T = (ICPPConstructor) col.getName(1).resolveBinding();
		assertTrue(CharArrayUtils.equals(T.getNameCharArray(),
				"T".toCharArray())); //$NON-NLS-1$
		assertEquals(T.getName(), "T"); //$NON-NLS-1$
	}
	
	// namespace NS {                
	//    class T {};                
	//    void f( T );               
	// }                             
	// NS::T parm;                   
	// int main() {                  
	//    f( parm );                 
	// }                             
	public void testArgumentDependantLookup() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPNamespace NS = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPClassType T = (ICPPClassType) col.getName(1).resolveBinding();
		IFunction f = (IFunction) col.getName(2).resolveBinding();
		IVariable parm = (IVariable) col.getName(8).resolveBinding();
		
		assertInstances(col, NS, 2);
		assertInstances(col, T, 4);
		assertInstances(col, f, 2);
		assertInstances(col, parm, 2);
	}
	
	// namespace NS1{                       
	//    void f( void * );                 
	// }                                    
	// namespace NS2{                       
	//    using namespace NS1;              
	//    class B {};                       
	//    void f( void * );                 
	// }                                    
	// class A : public NS2::B {} *a;       
	// int main() {                         
	//    f( a );                           
	// }                                    
	public void testArgumentDependantLookup_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction fref = (IFunction) col.getName(14).resolveBinding();
		IFunction f1 = (IFunction) col.getName(1).resolveBinding();
		IFunction f2 = (IFunction) col.getName(6).resolveBinding();
		
		assertSame(f2, fref);
		assertNotNull(f1);
		assertNotNull(f2);
	}
	
	// namespace { int i; } //1
	// void f(){ i; }          
	// namespace A {           
	//    namespace {          
	//       int i;    //2     
	//       int j;            
	//    }                    
	//    void g(){ i; }       
	// }                       
	// using namespace A;      
	// void h() {              
	//    i;    //ambiguous    
	//    A::i; //i2           
	//    j;                   
	// }                       
	public void testBug84610() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(17, col.size());
		
		IVariable i1 = (IVariable) col.getName(1).resolveBinding();
		IVariable i2 = (IVariable) col.getName(6).resolveBinding();
		IVariable j = (IVariable) col.getName(7).resolveBinding();
		
		assertInstances(col, i1, 2);
		assertInstances(col, i2, 4);
		assertInstances(col, j, 2);
		
		IProblemBinding problem = (IProblemBinding) col.getName(12)
		.resolveBinding();
		assertEquals(IProblemBinding.SEMANTIC_AMBIGUOUS_LOOKUP, problem.getID());
	}
	
	// struct B {          
	//    void mutate();   
	// };                  
	// void g() {          
	//    B* pb = new B(); 
	//    pb->mutate();    
	// }                   
	public void testBug84703() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(8, col.size());
		
		ICPPMethod mutate = (ICPPMethod) col.getName(1).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(0).resolveBinding();
		IVariable pb = (IVariable) col.getName(4).resolveBinding();
		
		assertInstances(col, pb, 2);
		assertInstances(col, mutate, 2);
		assertInstances(col, B, 2);
	}
	
	// struct S { int i; };        
	// void f() {         ;        
	//    int S::* pm = &S::i;      
	// }                           
	public void testBug84469() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(9, col.size());
	}
	
	public void testPointerToMemberType() throws Exception {
		IASTTranslationUnit tu = parse("struct S; int S::* pm;", //$NON-NLS-1$
				ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(4, col.size());
		
		IVariable pm = (IVariable) col.getName(3).resolveBinding();
		ICPPClassType S = (ICPPClassType) col.getName(0).resolveBinding();
		
		IType t = pm.getType();
		assertNotNull(t);
		assertTrue(t instanceof ICPPPointerToMemberType);
		IType cls = ((ICPPPointerToMemberType) t).getMemberOfClass();
		assertSame(S, cls);
		assertTrue(((ICPPPointerToMemberType) t).getType() instanceof IBasicType);
	}
	
	// struct S { int i ; } *s; 
	// int S::* pm = &S::i;     
	// void f() {               
	//    s->*pm = 1;           
	// }                        
	public void testBug_PM_() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IBinding ref = col.getName(11).resolveBinding();
		IVariable pm = (IVariable) col.getName(5).resolveBinding();
		
		assertSame(pm, ref);
	}
	
	// struct S {               
	//    int i;                
	//    S* f();               
	// } *s;                    
	// S* (S::* pm) () = &S::f; 
	// void foo() {             
	//    (s->*pm)()->i;        
	// }                        
	public void testBug_PM_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType S = (ICPPClassType) col.getName(0).resolveBinding();
		IVariable pm = (IVariable) col.getName(8).resolveBinding();
		IField i = (IField) col.getName(1).resolveBinding();
		ICPPMethod f = (ICPPMethod) col.getName(3).resolveBinding();
		
		IType t = pm.getType();
		assertTrue(t instanceof ICPPPointerToMemberType);
		IFunctionType ft = (IFunctionType) ((ICPPPointerToMemberType) t).getType();
		IType ST = ((ICPPPointerToMemberType) t).getMemberOfClass();
		
		assertTrue(ft.getReturnType() instanceof IPointerType);
		assertSame(ST, ((IPointerType) ft.getReturnType()).getType());
		assertSame(S, ST);
		
		assertInstances(col, S, 5);
		assertInstances(col, pm, 2);
		assertInstances(col, i, 2);
		assertInstances(col, f, 3);
	}
	
	// public void testFindTypeBinding_1() throws Exception {
	// IASTTranslationUnit tu = parse(
	// "int x = 5; int y(x);", ParserLanguage.CPP); //$NON-NLS-1$
	//
	// IASTStandardFunctionDeclarator fdtor = (IASTStandardFunctionDeclarator)
	// ((IASTSimpleDeclaration) tu
	// .getDeclarations()[1]).getDeclarators()[0];
	// IASTName name = fdtor.getParameters()[0].getDeclarator().getName();
	// IBinding binding = CPPSemantics.findTypeBinding(tu, name);
	// assertNull(binding);
	//
	// tu = parse("struct x; int y(x);", ParserLanguage.CPP); //$NON-NLS-1$
	//
	// fdtor = (IASTStandardFunctionDeclarator) ((IASTSimpleDeclaration) tu
	// .getDeclarations()[1]).getDeclarators()[0];
	// name = ((ICPPASTNamedTypeSpecifier) fdtor.getParameters()[0]
	// .getDeclSpecifier()).getName();
	// binding = CPPSemantics.findTypeBinding(tu, name);
	// assertNotNull(binding);
	// assertTrue(binding instanceof ICPPClassType);
	// }
	//
	// public void testFindTypeBinding_2() throws Exception {
	// IASTTranslationUnit tu = parse(
	// "struct B; void f() { B * bp; }", ParserLanguage.CPP); //$NON-NLS-1$
	// IASTCompoundStatement compound = (IASTCompoundStatement)
	// ((IASTFunctionDefinition) tu
	// .getDeclarations()[1]).getBody();
	// IASTBinaryExpression b = (IASTBinaryExpression)
	// ((IASTExpressionStatement)compound.getStatements()[0]).getExpression();
	// IBinding binding =
	// ((IASTIdExpression)b.getOperand1()).getName().resolveBinding();
	// // IASTSimpleDeclaration decl = (IASTSimpleDeclaration)
	// ((IASTDeclarationStatement) compound
	// // .getStatements()[0]).getDeclaration();
	// // IBinding binding = CPPSemantics.findTypeBinding(compound,
	// // ((ICPPASTNamedTypeSpecifier)decl.getDeclSpecifier()).getName());
	// assertNotNull(binding);
	// assertTrue(binding instanceof ICPPClassType);
	// }
	
	// struct B { };
	// void g() {
	// B * bp;  //1
	// }
	public void testBug85049() throws Exception {
		IASTTranslationUnit t = parse(getAboveComment(), ParserLanguage.CPP);
		IASTFunctionDefinition g = (IASTFunctionDefinition) t.getDeclarations()[1];
		IASTCompoundStatement body = (IASTCompoundStatement) g.getBody();
		assertTrue(body.getStatements()[0] instanceof IASTDeclarationStatement);
	}
	
	// class A { public: int i; };             
	// class B : public A {};                  
	// void f( int B::* );                     
	// void g() {                              
	//    int A::* pm = &A::i;                 
	//    f( pm );                             
	// }                                       
	public void testPMConversions() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(15).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPField i = (ICPPField) col.getName(1).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		IVariable pm = (IVariable) col.getName(11).resolveBinding();
		
		assertInstances(col, f, 2);
		assertInstances(col, A, 4);
		assertInstances(col, i, 3);
		assertInstances(col, B, 2);
		assertInstances(col, pm, 2);
	}
	
	// namespace N {                            
	//    class A { public: int i; };           
	//    void f( int A::* );                   
	// }                                        
	// int N::A::* pm = &N::A::i;               
	// void g() {                               
	//    f( pm );                              
	// }                                        
	public void testPMKoenig() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(16).resolveBinding();
		ICPPNamespace N = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(1).resolveBinding();
		
		assertInstances(col, f, 2);
		assertInstances(col, N, 3);
		assertInstances(col, A, 4);
	}
	
	// namespace M {                            
	//    class B { };                          
	//    void f( B* );                         
	// }                                        
	// namespace N {                            
	//    class A { public: M::B * b; };        
	// }                                        
	// M::B* N::A::* pm = &N::A::b;             
	// void g() {                               
	//    N::A * a;                             
	//    f( a->*pm );                          
	// }                                        
	public void testPMKoenig_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(27).resolveBinding();
		ICPPNamespace M = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(1).resolveBinding();
		ICPPNamespace N = (ICPPNamespace) col.getName(5).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(6).resolveBinding();
		IVariable pm = (IVariable) col.getName(17).resolveBinding();
		
		assertInstances(col, f, 2);
		assertInstances(col, M, 3);
		assertInstances(col, B, 6);
		assertInstances(col, N, 4);
		assertInstances(col, A, 5);
		assertInstances(col, pm, 2);
	}
	
	// class A {                       
	//    friend void set();           
	//    friend class B;              
	// };                              
	// void set();                     
	// class B{};                      
	public void testFriend_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		IFunction set = (IFunction) col.getName(1).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		
		assertInstances(col, set, 2);
		assertInstances(col, B, 2);
		
		IBinding[] friends = A.getFriends();
		assertEquals(2, friends.length);
		assertSame(friends[0], set);
		assertSame(friends[1], B);
	}

	// class Other {
	//    void m();
	// class A {                       
	//    friend void set();           
	//    friend void Other::m();             
	// };                              
	public void testFriend_Bug275358() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		ICPPClassType A = bh.assertNonProblem("A", 1);
		IFunction set = bh.assertNonProblem("set()", 3);
		IFunction m = bh.assertNonProblem("Other::m()", 8);
		
		IBinding[] friends = A.getFriends();
		assertEquals(2, friends.length);
		assertSame(friends[0], set);
		assertSame(friends[1], m);
		
		IBinding[] declaredMethods= A.getAllDeclaredMethods();
		assertEquals(0, declaredMethods.length);
		declaredMethods= A.getDeclaredMethods();
		assertEquals(0, declaredMethods.length);
	}

	// class A { friend class B; friend class B; }; 
	// class B{};                                   
	public void testBug59149() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		
		assertInstances(col, B, 3);
		
		IBinding[] friends = A.getFriends();
		assertEquals(friends.length, 1);
		assertSame(friends[0], B);
	}
	
	// class A {                      
	//    public: class N {};         
	// };                             
	// class B {                      
	//    friend class A::N;          
	// };                             
	public void testBug59302() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType N = (ICPPClassType) col.getName(5).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		assertInstances(col, N, 3);
		
		IBinding[] friends = B.getFriends();
		assertEquals(friends.length, 1);
		assertSame(friends[0], N);
		
		assertEquals(A.getFriends().length, 0);
		assertEquals(N.getFriends().length, 0);
	}
	
	// class A {                      
	//    friend class B *helper();   
	// };                             
	public void testBug75482() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction helper = (IFunction) col.getName(2).resolveBinding();
		assertSame(helper.getScope(), tu.getScope());
		
		ICPPClassType B = (ICPPClassType) col.getName(1).resolveBinding();
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		assertSame(B.getScope(), A.getScope());
		
		IBinding[] friends = A.getFriends();
		assertEquals(friends.length, 1);
		assertSame(friends[0], helper);
	}
	
	// void f( int );                         
	// void f( char );                        
	// void (*pf) (int) = &f;                 
	// void foo() {                           
	//    pf = &f;                            
	// }                                      
	public void testBug45763_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		IFunction f2 = (IFunction) col.getName(2).resolveBinding();
		IVariable pf = (IVariable) col.getName(4).resolveBinding();
		
		assertInstances(col, pf, 2);
		assertInstances(col, f1, 3);
		assertInstances(col, f2, 1);
	}
	
	// void f( char );                        
	// void f( int  );                        
	// void g( void (*)( int ) ) {}           
	// void (*pg)( void(*)(int) );            
	// void foo() {                           
	//    g( &f );                            
	//    (*pg)( &f );                        
	// }                                      
	public void testBug45763_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		IFunction f2 = (IFunction) col.getName(2).resolveBinding();
		IFunction g = (IFunction) col.getName(4).resolveBinding();
		IVariable pg = (IVariable) col.getName(7).resolveBinding();
		
		assertInstances(col, f1, 1);
		assertInstances(col, f2, 3);
		assertInstances(col, g, 2);
		assertInstances(col, pg, 2);
	}
	
	// void f( int );                  
	// void f( char );                 
	// void (* bar () ) ( int ) {      
	//    return &f;                   
	// }                               
	public void testBug45763_3() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		IFunction f2 = (IFunction) col.getName(2).resolveBinding();
		IFunction bar = (IFunction) col.getName(4).resolveBinding();
		assertNotNull(bar);
		
		assertInstances(col, f1, 2);
		assertInstances(col, f2, 1);
	}
	
	// void f( int );                  
	// void f( char );                 
	// void foo () {                   
	//    ( void (*)(int) ) &f;        
	// }                               
	public void testBug45763_4() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		IFunction f2 = (IFunction) col.getName(2).resolveBinding();
		IFunction bar = (IFunction) col.getName(4).resolveBinding();
		assertNotNull(bar);
		
		assertInstances(col, f1, 2);
		assertInstances(col, f2, 1);
	}
	
	// extern int g;       
	// int g;              
	// void f() {  g = 1; }
	public void testBug85824() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable g = (IVariable) col.getName(3).resolveBinding();
		assertInstances(col, g, 3);
	}
	
	// struct A {            
	//     int a2;           
	// };                    
	// struct B : public A { 
	//     int a1;           
	//     void f();         
	// };                    
	// int a3;               
	// void B::f(){          
	//    int a4;            
	//    a;                 
	// }                     
	public void testPrefixLookup() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IASTName name = col.getName(11);
		assertEquals("a", name.toString());
		IBinding[] bs = CPPSemantics.findBindingsForContentAssist(name, true);
		
		// check the result
		HashSet result= new HashSet();
		for (IBinding binding : bs) {
			result.add(binding.getName());
		}
		assertTrue(result.contains("a1"));
		assertTrue(result.contains("a2"));
		assertTrue(result.contains("a3"));
		assertTrue(result.contains("a4"));
		assertTrue(result.contains("A"));
		assertEquals(7, bs.length); // the bindings above + 2 constructors
	}
	
	// static void f();    
	// void f() {}         
	public void testIsStatic() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(1).resolveBinding();
		assertTrue(f.isStatic());
		assertInstances(col, f, 2);
	}
	
	//	// void f() {
	//	// if (__io.flags() & ios_base::showbase
	//	// || __i < 2 || __sign.size() > 1
	//	// || ((static_cast<part>(__p.field[3]) !=
	//	// money_base::none)
	//	// && __i == 2)) 
	//	// return;
	//	// }
	//	public void testBug85310() throws Exception {
	//		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
	//		IASTFunctionDefinition f = (IASTFunctionDefinition)
	//		tu.getDeclarations()[0];
	//		IASTCompoundStatement body = (IASTCompoundStatement) f.getBody();
	//		IASTCPPIfStatement if_stmt = (IASTCPPIfStatement) body.getStatements()[0];
	//		assertNotNull( if_stmt.getCondition() );
	//	}
	
	// struct B { void mutate(); };     
	// struct D1 : B {};                
	// struct D2 : B {};                
	// void B::mutate() {               
	//    new (this) D2;                
	// }                                
	// void g() {                       
	//    B* pb = new (p) D1;           
	// }                                
	public void testBug86267() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType D1 = (ICPPClassType) col.getName(2).resolveBinding();
		ICPPClassType D2 = (ICPPClassType) col.getName(4).resolveBinding();
		
		ICPPConstructor[] ctors = D1.getConstructors();
		ICPPConstructor d1_ctor = ctors[0];
		
		ctors = D2.getConstructors();
		ICPPConstructor d2_ctor = ctors[0];
		
		assertInstances(col, d1_ctor, 1);
		assertInstances(col, d2_ctor, 1);
	}
	
	// struct C {                                  
	//    void f();                                
	//    const C& operator =( const C& );         
	// };                                          
	// const C& C::operator = ( const C& other ) { 
	//    if( this != &other ) {                   
	//       this->~C();                           
	//       new (this) C(other);                  
	//       f();                                  
	//    }                                        
	//    return *this;                            
	// }                                           
	public void testBug86269() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType C = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPMethod f = (ICPPMethod) col.getName(1).resolveBinding();
		ICPPMethod op = (ICPPMethod) col.getName(3).resolveBinding();
		IParameter other = (IParameter) col.getName(5).resolveBinding();
		
		assertInstances(col, C, 6);
		assertInstances(col, f, 2);
		assertInstances(col, op, 3);
		assertInstances(col, other, 4);
		
		assertEquals(other.getName(), "other"); //$NON-NLS-1$
	}
	
	// extern "C" {                               
	//    void printf( const char * );              
	//    void sprintf( const char * );             
	// }                                            
	// void foo(){                                  
	//    char *p;                                  
	//    printf( p );                              
	//    printf( "abc" );                        
	// }                                            
	public void testBug86279() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction r1 = (IFunction) col.getName(6).resolveBinding();
		IFunction r2 = (IFunction) col.getName(8).resolveBinding();
		IFunction printf = (IFunction) col.getName(0).resolveBinding();
		
		assertSame(printf, r1);
		assertSame(printf, r2);
	}
	
	// struct S;            
	// extern S a;          
	// void g( S );         
	// void h() {           
	//    g( a );           
	// }                    
	public void testBug86346() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType S = (ICPPClassType) col.getName(0).resolveBinding();
		IFunction g = (IFunction) col.getName(3).resolveBinding();
		
		assertInstances(col, S, 3);
		assertInstances(col, g, 2);
	}
	
	public void testBug86288() throws Exception {
		String code = "int *foo( int *b ) { return (int *)(b); }"; //$NON-NLS-1$
		IASTTranslationUnit tu = parse(code, ParserLanguage.CPP);
		IASTReturnStatement r = (IASTReturnStatement) ((IASTCompoundStatement) ((IASTFunctionDefinition) tu
				.getDeclarations()[0]).getBody()).getStatements()[0];
		assertTrue(r.getReturnValue() instanceof IASTCastExpression);
	}
	
	// void foo() {
	// struct B {
	// int f();
	// };    			
	// int (B::*pb)() = &B::f;
	// }
	public void testBug84476() throws Exception {
		String code = getAboveComment();
		IASTFunctionDefinition foo = (IASTFunctionDefinition) parse(code,
				ParserLanguage.CPP).getDeclarations()[0];
		IASTDeclarationStatement decl = (IASTDeclarationStatement) ((IASTCompoundStatement) foo
				.getBody()).getStatements()[1];
		IASTSimpleDeclaration pb = (IASTSimpleDeclaration) decl
		.getDeclaration();
		IASTDeclarator d = pb.getDeclarators()[0];
		assertEquals(d.getNestedDeclarator().getPointerOperators().length, 1);
		assertEquals(d.getNestedDeclarator().getName().toString(), "pb"); //$NON-NLS-1$
		assertTrue(d.getNestedDeclarator().getPointerOperators()[0] instanceof ICPPASTPointerToMember);
	}
	
	// struct T1 {                   
	//    T1 operator() ( int x ) {  
	//       return T1(x);           
	//    }                          
	//    T1( int ) {}               
	// };                            
	public void testBug86336() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPConstructor T1_ctor = (ICPPConstructor) col.getName(6)
		.resolveBinding();
		ICPPClassType T1 = (ICPPClassType) col.getName(0).resolveBinding();
		
		assertInstances(col, T1_ctor, 2);
		assertInstances(col, T1, 2);
	}
	
	// struct S { int i; };    
	// void foo() {            
	//    int S::* pm = &S::i; 
	// }                       
	public void testBug86306() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType S = (ICPPClassType) col.getName(0).resolveBinding();
		
		assertInstances(col, S, 3);
		
		IASTName[] refs = tu.getReferences(S);
		assertEquals(refs.length, 2);
		assertSame(refs[0], col.getName(4));
		assertSame(refs[1], col.getName(7));
	}
	
	// class A {                           
	// 	public:                            
	//    template <class T> void f(T);    
	//    template <class T> struct X { }; 
	// };                                  
	// class B : public A {                
	//  public:                            
	//    using A::f<double>; // illformed 
	//    using A::X<int>; // illformed    
	// };                                  
	public void testBug86372() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
	}
	
	// void foo() {                    
	//    int i = 42;                  
	//    int a[10];                   
	//    for( int i = 0; i < 10; i++ )
	//       a[i] = 1;                 
	//    int j = i;                   
	// }                               
	public void testBug86319() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable i1 = (IVariable) col.getName(1).resolveBinding();
		IVariable i2 = (IVariable) col.getName(3).resolveBinding();
		
		assertNotSame(i1, i2);
		assertInstances(col, i1, 2);
		assertInstances(col, i2, 4);
	}
	
	// class X { int i, j; };           
	// class Y { X x; };                
	// void foo() {                     
	//    const Y y;                    
	//    y.x.i++;                      
	//    y.x.j++;                      
	//    Y* p = const_cast<Y*>(&y);    
	//    p->x.i;                       
	//    p->x.j;                       
	// }                                
	public void testBug86350() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField i = (ICPPField) col.getName(1).resolveBinding();
		ICPPField j = (ICPPField) col.getName(2).resolveBinding();
		ICPPField x = (ICPPField) col.getName(5).resolveBinding();
		
		assertInstances(col, i, 3);
		assertInstances(col, j, 3);
		assertInstances(col, x, 5);
	}
	
	// void foo() {
	//   struct A {
	//     int val;
	//     A(int i) : val(i) { }
	//     ~A() { }
	//     operator bool() { return val != 0; }
	//   };
	//   int i = 1;
	//   while (A a = i) {
	//     i = 0;
	//   }
	// }
	public void testBug84478() throws Exception {
		IASTFunctionDefinition foo = (IASTFunctionDefinition) parse(
				getAboveComment(), ParserLanguage.CPP).getDeclarations()[0];
		ICPPASTWhileStatement whileStatement = (ICPPASTWhileStatement) ((IASTCompoundStatement) foo
				.getBody()).getStatements()[2];
		assertNull(whileStatement.getCondition());
		assertNotNull(whileStatement.getConditionDeclaration());
	}
	
	// void foo() {                      
	//    const int x = 12;              
	//    {   enum { x = x };  }         
	// }                                 
	// enum { RED };                     
	public void testBug86353() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IEnumerator enum_x = (IEnumerator) col.getName(3).resolveBinding();
		IBinding x_ref = col.getName(4).resolveBinding();
		IEnumerator RED = (IEnumerator) col.getName(6).resolveBinding();
		
		String[] s = ((ICPPBinding) RED).getQualifiedName();
		assertEquals(s[0], "RED"); //$NON-NLS-1$
		assertTrue(((ICPPBinding) RED).isGloballyQualified());
		
		IASTName[] decls = tu.getDeclarationsInAST(enum_x);
		assertEquals(decls.length, 1);
		assertSame(decls[0], col.getName(3));
		
		decls = tu.getDeclarationsInAST(x_ref);
		assertEquals(decls.length, 1);
		assertSame(decls[0], col.getName(1));
		
		decls = tu.getDeclarationsInAST(RED);
		assertEquals(decls.length, 1);
		assertSame(decls[0], col.getName(6));
	}
	
	// class D {};                   
	// D d1;                         
	// const D d2;                   
	// void foo() {                  
	//    typeid(d1) == typeid(d2);  
	//    typeid( D ) == typeid(d2); 
	// }                             
	public void testBug86274() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertEquals(col.size(), 10);
		
		IVariable d1 = (IVariable) col.getName(6).resolveBinding();
		IVariable d2 = (IVariable) col.getName(7).resolveBinding();
		ICPPClassType D = (ICPPClassType) col.getName(8).resolveBinding();
		
		assertInstances(col, D, 4);
		assertInstances(col, d1, 2);
		assertInstances(col, d2, 3);
	}
	
	// void point ( int = 3, int = 4 );        
	// void foo() {                            
	//    point( 1, 2 );                       
	//    point( 1 );                          
	//    point( );                            
	// }                                       
	public void testBug86546() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction point = (IFunction) col.getName(0).resolveBinding();
		
		assertInstances(col, point, 4);
	}
	
	// namespace Outer{                   
	//    int i;                          
	//    namespace Inner {               
	//       void f() {  i++;  }          
	//       int i;                       
	//       void g() {  i++;  }          
	//    }                               
	// }                                  
	public void testBug86358_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable i = (IVariable) col.getName(4).resolveBinding();
		IVariable i2 = (IVariable) col.getName(7).resolveBinding();
		
		assertInstances(col, i, 2);
		assertInstances(col, i2, 2);
	}
	
	// namespace Q {                    
	//    namespace V {                 
	//       void f();                  
	//    }                             
	//    void V::f() {}                
	//    namespace V {                 
	//    }                             
	// }                                
	public void testBug86358_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(2).resolveBinding();
		ICPPFunction f2 = (ICPPFunction) col.getName(5).resolveBinding();
		assertSame(f1, f2);
		
		IASTName[] decls = tu.getDeclarationsInAST(f2);
		assertEquals(decls.length, 2);
		assertSame(decls[0], col.getName(2));
		assertSame(decls[1], col.getName(5));
		
		String[] s = f2.getQualifiedName();
		assertEquals(s[0], "Q"); //$NON-NLS-1$
		assertEquals(s[1], "V"); //$NON-NLS-1$
		assertEquals(s[2], "f"); //$NON-NLS-1$
		assertTrue(f2.isGloballyQualified());
	}
	
	// struct B {                          
	//    void f ( char );                 
	//    void g ( char );                 
	// };                                  
	// struct D : B {                      
	//    using B::f;                      
	//    void f( int ) { f('c'); }        
	//    void g( int ) { g('c'); }        
	// };                                  
	public void test86371() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction f_ref = (ICPPFunction) col.getName(12).resolveBinding();
		ICPPFunction g_ref = (ICPPFunction) col.getName(15).resolveBinding();
		
		ICPPFunction f = (ICPPFunction) col.getName(1).resolveBinding();
		assertSame(f_ref, f);
		
		IFunction g = (IFunction) col.getName(13).resolveBinding();
		assertSame(g, g_ref);
		
		assertInstances(col, f_ref, 2);
		assertInstances(col, g_ref, 2);
		
		String[] s = f_ref.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertEquals(s[1], "f"); //$NON-NLS-1$
		assertTrue(f_ref.isGloballyQualified());
		
		s = g_ref.getQualifiedName();
		assertEquals(s[0], "D"); //$NON-NLS-1$
		assertEquals(s[1], "g"); //$NON-NLS-1$
		assertTrue(f.isGloballyQualified());
	}
	
	// namespace Company_with_veryblahblah {}       
	// namespace CWVLN = Company_with_veryblahblah; 
	// namespace CWVLN = Company_with_veryblahblah; 
	// namespace CWVLN = CWVLN;                     
	public void testBug86369() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPNamespace ns = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPNamespace alias = (ICPPNamespace) col.getName(1).resolveBinding();
		
		String[] s = ns.getQualifiedName();
		assertEquals(s[0], "Company_with_veryblahblah"); //$NON-NLS-1$
		s = alias.getQualifiedName();
		assertEquals(s[0], "CWVLN"); //$NON-NLS-1$
		
		assertTrue(alias instanceof ICPPNamespaceAlias);
		assertSame(((ICPPNamespaceAlias) alias).getBinding(), ns);
		
		IASTName[] refs = tu.getReferences(ns);
		assertEquals(refs.length, 2);
		assertSame(refs[0], col.getName(2));
		assertSame(refs[1], col.getName(4));
		
		IASTName[] decls = tu.getDeclarationsInAST(ns);
		assertEquals(decls.length, 1);
		assertSame(decls[0], col.getName(0));
		
		refs = tu.getReferences(alias);
		assertEquals(refs.length, 1);
		assertSame(refs[0], col.getName(6));
		
		decls = tu.getDeclarationsInAST(alias);
		assertEquals(decls.length, 3);
		assertSame(decls[0], col.getName(1));
		assertSame(decls[1], col.getName(3));
		assertSame(decls[2], col.getName(5));
	}
	
	// namespace A {                  
	//    void f( char );             
	//    void f( int );              
	// }                              
	// using A::f;                    
	public void testBug86470_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPUsingDeclaration u = (ICPPUsingDeclaration) col.getName(7)
		.resolveBinding();
		
		IASTName[] decls = tu.getDeclarationsInAST(u);
		assertEquals(3, decls.length);	// 2 function-decls + using-decl
		assertSame(decls[0], col.getName(1));
		assertSame(decls[1], col.getName(3));
		
		IBinding[] delegates = u.getDelegates();
		assertEquals(delegates.length, 2);
		
		decls = tu.getDeclarationsInAST(delegates[0]);
		assertEquals(2, decls.length);	// function-decl + using-decl
		assertSame(decls[0], col.getName(1));
		assertSame(decls[1], col.getName(7));
		
		decls = tu.getDeclarationsInAST(delegates[0]);
		assertEquals(2, decls.length);  // function-decl + using-decl
		assertSame(decls[0], col.getName(1));
	}
	
	// namespace A {                  
	//    void f( int );              
	//    void f( double );           
	// }                              
	// namespace B {                  
	//    void f( int );              
	//    void f( double );           
	//    void f( char );             
	// }                              
	// void g() {                     
	//    using A::f;                 
	//    using B::f;                 
	//    f( 'c' );                   
	// }                              
	public void testBug86470_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f_decl = (IFunction) col.getName(10).resolveBinding();
		IFunction f_ref = (IFunction) col.getName(19).resolveBinding();
		assertSame(f_decl, f_ref);
	}
	
	// namespace A {                       
	//    struct g {};                     
	//    void g ( char );                 
	// }                                   
	// void f() {                          
	//    using A::g;                      
	//    g('a');                          
	//    struct g gg;                     
	// }                                   
	public void testBug86470_3() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IBinding ref1 = col.getName(8).resolveBinding();
		IBinding ref2 = col.getName(9).resolveBinding();
		
		ICPPClassType g_struct = (ICPPClassType) col.getName(1)
		.resolveBinding();
		IFunction g_func = (IFunction) col.getName(2).resolveBinding();
		
		assertSame(g_struct,ref2);
		assertSame(g_func, ref1);
		
		ICPPUsingDeclaration comp = (ICPPUsingDeclaration) col.getName(7)
		.resolveBinding();
		IASTName[] decls = tu.getDeclarationsInAST(comp);
		assertEquals(3, decls.length);	// struct, func and using-decl
		assertSame(decls[0], col.getName(1));
		assertSame(decls[1], col.getName(2));
		assertSame(decls[2], col.getName(7));
	}
	
	// namespace A {                       
	//    int x;                           
	// }                                   
	// namespace B {                       
	//    struct x {};                     
	// }                                   
	// void f() {                          
	//    using A::x;                      
	//    using B::x;                      
	//    x = 1;                           
	//    struct x xx;                     
	// }                                   
	public void testBug86470_4() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPBinding ref1 = (ICPPBinding) col.getName(11).resolveBinding();
		ICPPBinding ref2 = (ICPPBinding) col.getName(12).resolveBinding();
		
		ICPPClassType x_struct = (ICPPClassType) col.getName(3)
		.resolveBinding();
		IVariable x_var = (IVariable) col.getName(1).resolveBinding();
		
		assertSame(x_struct, ref2);
		assertSame(x_var, ref1);
		
		IASTName[] refs = tu.getReferences(x_struct);
		assertEquals(2, refs.length);	// 1 ref + using-decl
		assertSame(refs[0], col.getName(10));
		assertSame(refs[1], col.getName(12));
		
		String[] s = ref2.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertEquals(s[1], "x"); //$NON-NLS-1$
		assertTrue(ref2.isGloballyQualified());
		
		s = x_struct.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertEquals(s[1], "x"); //$NON-NLS-1$
		assertTrue(x_struct.isGloballyQualified());
	}
	
	// namespace A {                       
	//    void f( int );                   
	//    void f( double );                
	// }                                   
	// void g() {                          
	//    void f( char );                  
	//    using A::f;                      
	//    f( 3.5 );                        
	// }                                   
	public void testBug86470_5() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction f = (ICPPFunction) col.getName(3).resolveBinding();
		
		ICPPFunction f_ref = (ICPPFunction) col.getName(11).resolveBinding();
		assertSame( f_ref, f);
		
		String[] s = f_ref.getQualifiedName();
		assertEquals(s[0], "A"); //$NON-NLS-1$
		assertEquals(s[1], "f"); //$NON-NLS-1$
		assertTrue(f_ref.isGloballyQualified());
		
		s = f.getQualifiedName();
		assertEquals(s[0], "A"); //$NON-NLS-1$
		assertEquals(s[1], "f"); //$NON-NLS-1$
		assertTrue(f.isGloballyQualified());
	}
	
	// class B;                                 
	// class A {                                
	//    int i;                                
	//    friend void f( B * );                 
	// };                                       
	// class B : public A {};                   
	// void f( B* p ) {                         
	//    p->i = 1;                             
	// }                                        
	public void testBug86678() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType B = (ICPPClassType) col.getName(6).resolveBinding();
		ICPPField i = (ICPPField) col.getName(12).resolveBinding();
		IParameter p = (IParameter) col.getName(10).resolveBinding();
		
		assertInstances(col, B, 4);
		assertInstances(col, i, 2);
		assertInstances(col, p, 3);
	}
	
	// int printf( const char *, ... );             
	// void foo(){                                  
	//    int a, b;                                 
	//    printf( "hello" );                      
	//    printf("a=%d b=%d", a, b );             
	// }                                            
	public void testBug86543() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction printf = (IFunction) col.getName(6).resolveBinding();
		assertInstances(col, printf, 3);
	}
	
	// int max( int a, int b, int c ) {                  
	//    int m = ( a > b ) ? a : b;                     
	//    return ( m > c ) ? m : c;                      
	// }                                                 
	public void testBug86554() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable m = (IVariable) col.getName(11).resolveBinding();
		IParameter a = (IParameter) col.getName(1).resolveBinding();
		IParameter b = (IParameter) col.getName(2).resolveBinding();
		IParameter c = (IParameter) col.getName(3).resolveBinding();
		
		String[] s = ((ICPPBinding) a).getQualifiedName();
		assertEquals(s[0], "a"); //$NON-NLS-1$
		assertFalse(((ICPPBinding) a).isGloballyQualified());
		
		assertInstances(col, m, 3);
		assertInstances(col, a, 3);
		assertInstances(col, b, 3);
		assertInstances(col, c, 3);
	}
	
	// int g();                               
	// struct X { static int g(); };          
	// struct Y : X { static int i ; };       
	// int Y::i = g();                        
	public void testBug86621() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction g1 = (ICPPFunction) col.getName(0).resolveBinding();
		ICPPMethod g2 = (ICPPMethod) col.getName(9).resolveBinding();
		
		String[] s = g1.getQualifiedName();
		assertEquals(s[0], "g"); //$NON-NLS-1$
		assertTrue(g1.isGloballyQualified());
		
		s = g2.getQualifiedName();
		assertEquals(s[0], "X"); //$NON-NLS-1$
		assertEquals(s[1], "g"); //$NON-NLS-1$
		assertTrue(g2.isGloballyQualified());
		
		assertInstances(col, g1, 1);
		assertInstances(col, g2, 2);
	}
	
	// class V { int f(); int x; };                 
	// class W { int g(); int y; };                 
	// class B : public virtual V, public W {       
	//    int f();  int x;                          
	//    int g();  int y;                          
	// };                                           
	// class C : public virtual V, public W {};     
	// class D : public B, public C {               
	//    void foo();                               
	// };                                           
	// void D::foo(){                               
	//    x++;                                      
	//    f();                                      
	//    y++;                                      
	//    g();                                      
	// }                                            
	public void testBug86649() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField x = (ICPPField) col.getName(23).resolveBinding();
		ICPPMethod f = (ICPPMethod) col.getName(24).resolveBinding();
		
		String[] s = f.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertEquals(s[1], "f"); //$NON-NLS-1$
		assertTrue(f.isGloballyQualified());
		
		s = x.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertEquals(s[1], "x"); //$NON-NLS-1$
		assertTrue(x.isGloballyQualified());
		
		IProblemBinding y = (IProblemBinding) col.getName(25).resolveBinding();
		IProblemBinding g = (IProblemBinding) col.getName(26).resolveBinding();
		
		assertEquals(y.getID(), IProblemBinding.SEMANTIC_AMBIGUOUS_LOOKUP);
		assertEquals(g.getID(), IProblemBinding.SEMANTIC_AMBIGUOUS_LOOKUP);
		
		assertInstances(col, x, 2);
		assertInstances(col, f, 2);
	}
	
	// struct C {                    
	//    int c;                     
	//    C() : c(0) { }             
	// };                            
	public void testBug86827() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPVariable c = (ICPPVariable) col.getName(1).resolveBinding();
		
		String[] s = c.getQualifiedName();
		assertEquals(s.length, 2);
		assertEquals(s[0], "C"); //$NON-NLS-1$
		assertEquals(s[1], "c"); //$NON-NLS-1$
		assertTrue(c.isGloballyQualified());
		
		IASTName[] refs = tu.getReferences(c);
		assertEquals(refs.length, 1);
		assertSame(refs[0], col.getName(3));
	}
	
	// void f( int par ) {            
	//    int v1;                     
	//    {                           
	//       int v2;                  
	//    }                           
	// }                              
	public void testFind_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPVariable v1 = (ICPPVariable) col.getName(2).resolveBinding();
		ICPPVariable v2 = (ICPPVariable) col.getName(3).resolveBinding();
		
		String[] s = v1.getQualifiedName();
		assertEquals(s[0], "v1"); //$NON-NLS-1$
		assertFalse(v1.isGloballyQualified());
		
		s = v2.getQualifiedName();
		assertEquals(s[0], "v2"); //$NON-NLS-1$
		assertFalse(v2.isGloballyQualified());
		
		ICPPBlockScope scope = (ICPPBlockScope) v2.getScope();
		IBinding[] bs = scope.find("v1"); //$NON-NLS-1$
		assertEquals(bs.length, 1);
		assertSame(bs[0], v1);
	}
	
	// class A { int a; };            
	// class B : public A {           
	//    void f();                   
	// };                             
	// void B::f() {                  
	// }                              
	public void testFind_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPField a = (ICPPField) col.getName(1).resolveBinding();
		ICPPMethod f = (ICPPMethod) col.getName(7).resolveBinding();
		
		IScope scope = f.getFunctionScope();
		IBinding[] bs = scope.find("a"); //$NON-NLS-1$
		assertEquals(bs.length, 1);
		assertSame(bs[0], a);
		
		bs = scope.find("~B"); //$NON-NLS-1$
		assertEquals(bs.length, 1);
		assertTrue(bs[0] instanceof ICPPMethod);
		assertTrue(bs[0].getName().equals("~B")); //$NON-NLS-1$
		
		bs = scope.find("A"); //$NON-NLS-1$
		assertEquals(bs.length, 1);
		assertSame(bs[0], A);
	}
	
	// namespace A {                       
	//    void f( int );                   
	//    void f( double );                
	// }                                   
	// void g() {                          
	//    void f( char );                  
	//    using A::f;                      
	// }                                   
	public void testFind_3() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(1).resolveBinding();
		IFunction f2 = (IFunction) col.getName(3).resolveBinding();
		IFunction f3 = (IFunction) col.getName(6).resolveBinding();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) col.getName(5).getParent().getParent();
		IScope scope = ((IASTCompoundStatement) def.getBody()).getScope();
		IBinding[] bs = scope.find("f"); //$NON-NLS-1$
		assertEquals(3, bs.length);
		assertSame(bs[0], f3);
		assertSame(bs[1], f1);
		assertSame(bs[2], f2);
		
		String[] s = ((ICPPBinding)  bs[1]).getQualifiedName();
		assertEquals(s.length, 2);
		assertEquals(s[0], "A"); //$NON-NLS-1$
		assertEquals(s[1], "f"); //$NON-NLS-1$
		assertTrue(((ICPPBinding)  bs[1]).isGloballyQualified());
	}
	
	// namespace A {                                  
	//    struct f;                                   
	//    void f();                                   
	// }                                              
	// namespace B {                                  
	//    void f( int );                              
	// }                                              
	// namespace C {                                  
	//    using namespace B;                          
	// }                                              
	// void g(){                                      
	//    using namespace A;                          
	//    using namespace C;                          
	// }                                              
	public void testFind_4() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType f = (ICPPClassType) col.getName(1).resolveBinding();
		IFunction f1 = (IFunction) col.getName(2).resolveBinding();
		IFunction f2 = (IFunction) col.getName(4).resolveBinding();
		
		IASTFunctionDefinition def = (IASTFunctionDefinition) col.getName(8)
		.getParent().getParent();
		IScope scope = ((IASTCompoundStatement) def.getBody()).getScope();
		IBinding[] bs = scope.find("f"); //$NON-NLS-1$
		assertEquals(3, bs.length);
		assertSame(bs[0], f);
		assertSame(bs[1], f1);
		assertSame(bs[2], f2);
	}
	
	// class A {
	// public:
	//    A();
	//    void f();
	// };
	// class B : public A {
	// public:
	//    B();
	//    void bf();
	// };
	public void testFind_bug185408() throws Exception {		
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(6).resolveBinding();
		IScope classScope= f1.getScope();
		assertTrue(classScope instanceof ICPPClassScope);
		IBinding[] bindings = classScope.find("bf");
		ICPPMethod method= extractSingleMethod(bindings);
		assertEquals(method.getQualifiedName()[0], "B");
		
		bindings= classScope.find("f");
		method= extractSingleMethod(bindings);
		assertEquals(method.getQualifiedName()[0], "A");
		
		bindings= classScope.find("B");
		ICPPClassType classType= extractSingleClass(bindings);
		assertEquals(classType.getQualifiedName()[0], "B");
		
		bindings= classScope.find("A");
		classType= extractSingleClass(bindings);
		assertEquals(classType.getQualifiedName()[0], "A");
	}
	
	private ICPPMethod extractSingleMethod(IBinding[] bindings) {
		assertEquals(1, bindings.length);
		assertTrue(bindings[0] instanceof ICPPMethod);
		return (ICPPMethod) bindings[0];
	}
	
	private ICPPClassType extractSingleClass(IBinding[] bindings) {
		assertEquals(1, bindings.length);
		assertTrue(bindings[0] instanceof ICPPClassType);
		return (ICPPClassType) bindings[0];
	}
	
	// class A {                                 
	//    int a;                                 
	//    void fa();                             
	// };                                        
	// class B : public A {                      
	//    int b;                                 
	//    void fb();                             
	// };                                        
	public void testGets() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(3).resolveBinding();
		ICPPField a = (ICPPField) col.getName(1).resolveBinding();
		ICPPMethod fa = (ICPPMethod) col.getName(2).resolveBinding();
		ICPPField b = (ICPPField) col.getName(5).resolveBinding();
		ICPPMethod fb = (ICPPMethod) col.getName(6).resolveBinding();
		
		Object[] result = B.getDeclaredFields();
		assertEquals(result.length, 1);
		assertSame(result[0], b);
		
		result = B.getFields();
		assertEquals(result.length, 2);
		assertSame(result[0], b);
		assertSame(result[1], a);
		
		result = B.getDeclaredMethods();
		assertEquals(result.length, 1);
		assertSame(result[0], fb);
		
		result = B.getAllDeclaredMethods();
		assertEquals(result.length, 2);
		assertSame(result[0], fb);
		assertSame(result[1], fa);
		
		ICPPMethod[] B_implicit = ((ICPPClassScope) B.getCompositeScope())
		.getImplicitMethods();
		assertEquals(B_implicit.length, 4);
		assertTrue(B_implicit[0].getName().equals("B")); //$NON-NLS-1$
		assertTrue(B_implicit[1].getName().equals("B")); //$NON-NLS-1$
		assertTrue(B_implicit[2].getName().equals("operator =")); //$NON-NLS-1$
		assertTrue(B_implicit[3].getName().equals("~B")); //$NON-NLS-1$
		
		ICPPMethod[] A_implicit = ((ICPPClassScope) A.getCompositeScope())
		.getImplicitMethods();
		assertEquals(A_implicit.length, 4);
		assertTrue(A_implicit[0].getName().equals("A")); //$NON-NLS-1$
		assertTrue(A_implicit[1].getName().equals("A")); //$NON-NLS-1$
		assertTrue(A_implicit[2].getName().equals("operator =")); //$NON-NLS-1$
		assertTrue(A_implicit[3].getName().equals("~A")); //$NON-NLS-1$
		
		result = B.getMethods();
		assertEquals(result.length, 10);
		assertSame(result[0], fb);
		assertSame(result[1], B_implicit[0]);
		assertSame(result[2], B_implicit[1]);
		assertSame(result[3], B_implicit[2]);
		assertSame(result[4], B_implicit[3]);
		assertSame(result[5], fa);
		assertSame(result[6], A_implicit[0]);
		assertSame(result[7], A_implicit[1]);
		assertSame(result[8], A_implicit[2]);
		assertSame(result[9], A_implicit[3]);
	}
	
	public void testBug87424() throws Exception {
		IASTTranslationUnit tu = parse(
				"int * __restrict x;", ParserLanguage.CPP, true); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable x = (IVariable) col.getName(0).resolveBinding();
		IType t = x.getType();
		assertTrue(t instanceof IGPPPointerType);
		assertTrue(((IGPPPointerType) t).isRestrict());
		
		tu = parse("class A {}; int A::* __restrict x;", ParserLanguage.CPP, true); //$NON-NLS-1$
		col = new CPPNameCollector();
		tu.accept(col);
		
		x = (IVariable) col.getName(3).resolveBinding();
		t = x.getType();
		assertTrue(t instanceof IGPPPointerToMemberType);
		assertTrue(((IGPPPointerToMemberType) t).isRestrict());
	}
	
	public void testBug87705() throws Exception {
		IASTTranslationUnit tu = parse(
				"class A { friend class B::C; };", ParserLanguage.CPP, true); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IProblemBinding B = (IProblemBinding) col.getName(2).resolveBinding();
		assertEquals(B.getID(), IProblemBinding.SEMANTIC_NAME_NOT_FOUND);
		IProblemBinding C = (IProblemBinding) col.getName(3).resolveBinding();
		assertEquals(C.getID(), IProblemBinding.SEMANTIC_BAD_SCOPE);
	}
	
	public void testBug88459() throws Exception {
		IASTTranslationUnit tu = parse("int f(); ", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(0).resolveBinding();
		assertFalse(f.isStatic());
	}
	
	public void testBug88501_1() throws Exception {
		IASTTranslationUnit tu = parse(
				"void f(); void f( int ); struct f;", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).resolveBinding() instanceof IFunction);
		assertTrue(col.getName(1).resolveBinding() instanceof IFunction);
		assertTrue(col.getName(3).resolveBinding() instanceof ICPPClassType);
	}
	
	// public void testBug8342_1() throws Exception {
	// IASTTranslationUnit tu = parse( "int a; int a;", ParserLanguage.CPP );
	// //$NON-NLS-1$
	// CPPNameCollector col = new CPPNameCollector();
	// tu.accept(col);
	//        
	// assertTrue( col.getName(0).resolveBinding() instanceof IVariable );
	// IProblemBinding p = (IProblemBinding) col.getName(1).resolveBinding();
	// assertEquals( p.getID(), IProblemBinding.SEMANTIC_INVALID_REDEFINITION );
	// }
	
	public void testBug8342_2() throws Exception {
		IASTTranslationUnit tu = parse(
				"extern int a; extern char a;", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).resolveBinding() instanceof IVariable);
		IProblemBinding p = (IProblemBinding) col.getName(1).resolveBinding();
		assertEquals(p.getID(), IProblemBinding.SEMANTIC_INVALID_REDECLARATION);
	}
	
	// namespace A { int i; }      
	// namespace B = A;            
	// void f() {                  
	//    B::i;                    
	// }                           
	public void testNamespaceAlias_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPNamespace A = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPNamespaceAlias alias = (ICPPNamespaceAlias) col.getName(6)
		.resolveBinding();
		ICPPVariable i = (ICPPVariable) col.getName(7).resolveBinding();
		
		assertInstances(col, A, 2);
		assertInstances(col, alias, 2);
		assertInstances(col, i, 3);
		
		String[] s = i.getQualifiedName();
		assertEquals(s[0], "A"); //$NON-NLS-1$
		assertEquals(s[1], "i"); //$NON-NLS-1$
		assertTrue(i.isGloballyQualified());
		
		s = alias.getQualifiedName();
		assertEquals(s[0], "B"); //$NON-NLS-1$
		assertTrue(alias.isGloballyQualified());
	}
	
	// class A{};              
	// class B : public A {    
	//    B () : A() {}        
	// };                      
	public void testBug89539() throws Exception {
	    String content= getAboveComment();
		IASTTranslationUnit tu = parse(content, ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A1 = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPClassType A2 = (ICPPClassType) col.getName(2).resolveBinding();
		assertSame(A1, A2);
		
		ICPPConstructor A3 = (ICPPConstructor) col.getName(4).resolveBinding();
		assertSame(A3.getScope(), A1.getCompositeScope());
		
		tu = parse(content, ParserLanguage.CPP); 
		col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(4).resolveBinding() instanceof ICPPConstructor);
	}
	
	// class B * b;            
	// class A {               
	//    A * a;               
	// };                      
	// class A;                
	public void testBug89851() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).resolveBinding() instanceof ICPPClassType);
		assertTrue(col.getName(1).resolveBinding() instanceof ICPPVariable);
		assertTrue(col.getName(2).resolveBinding() instanceof ICPPClassType);
		assertTrue(col.getName(3).resolveBinding() instanceof ICPPClassType);
	}
	
	public void testBug89828() throws Exception {
		IASTTranslationUnit tu = parse(
				"class B * b; void f();  void f( int );", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).resolveBinding() instanceof ICPPClassType);
		assertTrue(col.getName(1).resolveBinding() instanceof ICPPVariable);
		IFunction f1 = (IFunction) col.getName(2).resolveBinding();
		IFunction f2 = (IFunction) col.getName(3).resolveBinding();
		
		IScope scope = tu.getScope();
		IBinding[] bs = scope.find("f"); //$NON-NLS-1$
		assertEquals(bs.length, 2);
		assertSame(bs[0], f1);
		assertSame(bs[1], f2);
	}
	
	// class A {                  
	//    enum type { t1, t2 };   
	//    void f( type t );       
	// };                         
	// class B : public A {       
	//    void g() {              
	//       f( A::t1 );          
	//    }                       
	// };                         
	public void testBug90039() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(10).resolveBinding();
		IEnumerator t1 = (IEnumerator) col.getName(13).resolveBinding();
		
		assertInstances(col, f, 2);
		assertInstances(col, t1, 3);
	}
	
	// void f( void ) {                 
	//    enum { one };                 
	// }                                
	public void testBug90039_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertTrue(col.getName(0).resolveBinding() instanceof IFunction);
		assertTrue(col.getName(1).resolveBinding() instanceof IParameter);
		IEnumeration e = (IEnumeration) col.getName(2).resolveBinding();
		IEnumerator one = (IEnumerator) col.getName(3).resolveBinding();
		assertSame(one.getType(), e);
	}
	
	// class Foo {
	// public:
	// operator int();
	// char& operator[](unsigned int);
	// };
	public void testOperatorConversionNames() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IASTName name1 = col.getName(1);
		IASTName name2 = col.getName(2);
		
		assertNotNull(name1);
		assertNotNull(name2);
		
		assertTrue(name1 instanceof ICPPASTConversionName);
		assertTrue(name2 instanceof ICPPASTOperatorName);
		
		IASTTypeId typeId = ((ICPPASTConversionName) name1).getTypeId();
		assertNotNull(typeId);
		assertEquals(((IASTSimpleDeclSpecifier) typeId.getDeclSpecifier())
				.getType(), IASTSimpleDeclSpecifier.t_int);
		
	}
	
	// class X { operator int(); }; 
	// X::operator int() { } 
	// template <class A,B> class X<A,C> { operator int(); }; 
	// template <class A,B> X<A,C>::operator int() { } 
	public void testBug36769B() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		// 1,4,12,21 - conversion
		// 2, 16 .isConversion
				
		final IASTName int1 = col.getName(1);
		assertNotNull(int1);
		assertTrue(int1 instanceof ICPPASTConversionName);
		assertNotNull(((ICPPASTConversionName) int1).getTypeId());
		
		IASTFunctionDefinition fdef= getDeclaration(tu, 1);
		final IASTName x_int = fdef.getDeclarator().getName();
		assertNotNull(x_int);
		assertTrue(x_int instanceof ICPPASTQualifiedName);
		assertTrue(((ICPPASTQualifiedName) x_int).isConversionOrOperator());

		final IASTName int2= ((ICPPASTQualifiedName)x_int).getLastName();
		assertNotNull(int2);
		assertTrue(int2 instanceof ICPPASTConversionName);
		assertNotNull(((ICPPASTConversionName) int2).getTypeId());
		
		final IASTName int3 = col.getName(12);
		assertNotNull(int3);
		assertTrue(int3 instanceof ICPPASTConversionName);
		assertNotNull(((ICPPASTConversionName) int3).getTypeId());

		ICPPASTTemplateDeclaration tdef= getDeclaration(tu, 3);
		fdef= (IASTFunctionDefinition) tdef.getDeclaration();
		final IASTName x_ac_int = fdef.getDeclarator().getName();
		assertNotNull(x_ac_int);
		assertTrue(x_ac_int instanceof ICPPASTQualifiedName);
		assertTrue(((ICPPASTQualifiedName) x_ac_int).isConversionOrOperator());

		final IASTName int4= ((ICPPASTQualifiedName)x_ac_int).getLastName();
		assertNotNull(int4);
		assertTrue(int4 instanceof ICPPASTConversionName);
		assertNotNull(((ICPPASTConversionName) int4).getTypeId());
	}
	
	public void testBug88662() throws Exception {
		IASTTranslationUnit tu = parse(
				"int foo() {  return int();}", ParserLanguage.CPP); //$NON-NLS-1$
		IASTReturnStatement returnStatement = (IASTReturnStatement) ((IASTCompoundStatement) ((IASTFunctionDefinition) tu
				.getDeclarations()[0]).getBody()).getStatements()[0];
		ICPPASTSimpleTypeConstructorExpression expression = (ICPPASTSimpleTypeConstructorExpression) returnStatement
		.getReturnValue();
		assertEquals(expression.getInitialValue(), null);
		assertEquals(expression.getSimpleType(),
				ICPPASTSimpleTypeConstructorExpression.t_int);
	}
	
	public void testBug90498_1() throws Exception {
		IASTTranslationUnit tu = parse(
				"typedef int INT;\ntypedef INT ( FOO ) (INT);", ParserLanguage.CPP); //$NON-NLS-1$
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[1];
		IASTDeclSpecifier declSpec = decl.getDeclSpecifier();
		assertTrue(declSpec instanceof ICPPASTNamedTypeSpecifier);
		assertEquals(((ICPPASTNamedTypeSpecifier) declSpec).getName()
				.toString(), "INT"); //$NON-NLS-1$
				
		IASTDeclarator dtor = decl.getDeclarators()[0];
		assertTrue(dtor instanceof IASTFunctionDeclarator);
		assertNotNull(dtor.getNestedDeclarator());
		IASTDeclarator nested = dtor.getNestedDeclarator();
		assertEquals(nested.getName().toString(), "FOO"); //$NON-NLS-1$
	}
	
	public void testBug90498_2() throws Exception {
		IASTTranslationUnit tu = parse(
				"int (* foo) (int) (0);", ParserLanguage.CPP); //$NON-NLS-1$
		
		IASTSimpleDeclaration decl = (IASTSimpleDeclaration) tu
		.getDeclarations()[0];
		IASTDeclSpecifier declSpec = decl.getDeclSpecifier();
		assertTrue(declSpec instanceof IASTSimpleDeclSpecifier);
		
		IASTDeclarator dtor = decl.getDeclarators()[0];
		assertTrue(dtor instanceof IASTFunctionDeclarator);
		assertNotNull(dtor.getNestedDeclarator());
		IASTDeclarator nested = dtor.getNestedDeclarator();
		assertEquals(nested.getName().toString(), "foo"); //$NON-NLS-1$
		
		assertNotNull(dtor.getInitializer());
	}
	
	// class D { /* ... */ };
	// D d1;
	// const D d2;
	// void foo() {
	//     typeid(d1) == typeid(d2);
	//     typeid(D) == typeid(d2);
	// }
	public void testBug866274() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTFunctionDefinition foo = (IASTFunctionDefinition) tu
		.getDeclarations()[3];
		IASTCompoundStatement cs = (IASTCompoundStatement) foo.getBody();
		IASTStatement[] subs = cs.getStatements();
		for (int i = 0; i < subs.length; ++i) {
			IASTBinaryExpression be = (IASTBinaryExpression) ((IASTExpressionStatement) subs[i])
			.getExpression();
			if (i == 1) {
				IASTTypeIdExpression expression = (IASTTypeIdExpression) be
				.getOperand1();
				IASTTypeId typeId = expression.getTypeId();
				assertTrue(((IASTNamedTypeSpecifier) typeId.getDeclSpecifier())
						.getName().resolveBinding() instanceof IType);
			} else {
				IASTUnaryExpression expression = (IASTUnaryExpression) be
				.getOperand1();
				IASTIdExpression idExpression = (IASTIdExpression) expression
				.getOperand();
				assertTrue(idExpression.getName().resolveBinding() instanceof IVariable);
			}
			IASTUnaryExpression expression = (IASTUnaryExpression) be
			.getOperand2();
			IASTIdExpression idExpression = (IASTIdExpression) expression
			.getOperand();
			assertTrue(idExpression.getName().resolveBinding() instanceof IVariable);
			
		}
	}
	
	public void testTypedefFunction() throws Exception {
		IASTTranslationUnit tu = parse(
				"typedef int foo (int);", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IBinding binding = col.getName(0).resolveBinding();
		assertTrue(binding instanceof ITypedef);
		assertTrue(((ITypedef) binding).getType() instanceof IFunctionType);
	}
	
	// void f( int );          
	// void foo(){             
	//    f( ( 1, 2 ) );       
	// }                       
	public void testBug90616() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		IFunction f2 = (IFunction) col.getName(3).resolveBinding();
		assertSame(f1, f2);
	}
	
	public void testBug90603() throws Exception {
		IASTTranslationUnit tu = parse(
				"class X { void f(){} };", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType X = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPMethod f1 = (ICPPMethod) col.getName(1).resolveBinding();
		
		assertFalse(f1.isStatic());
		
		String[] qns = f1.getQualifiedName();
		assertEquals(qns.length, 2);
		assertEquals(qns[0], "X"); //$NON-NLS-1$
		assertEquals(qns[1], "f"); //$NON-NLS-1$
		assertTrue(f1.isGloballyQualified());
		assertEquals(f1.getVisibility(), ICPPMember.v_private);
		
		assertSame(f1.getScope(), X.getCompositeScope());
	}
	
	// class X {   };           
	// X x;                     
	// class X {   };           
	public void testBug90662() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType X = (ICPPClassType) col.getName(0).resolveBinding();
		IVariable x = (IVariable) col.getName(2).resolveBinding();
		IProblemBinding problem = (IProblemBinding) col.getName(3)
		.resolveBinding();
		assertSame(x.getType(), X);
		assertEquals(problem.getID(),
				IProblemBinding.SEMANTIC_INVALID_REDEFINITION);
	}
	
	// struct C {                               
	//    void* operator new [ ] (unsigned int);
	//    void* operator new (unsigned int);
	//    void operator delete [ ] ( void * );       
	//    void operator delete (void *);
	//    const C& operator+=(const C&);
	//    const C& operator -= (const C&);
	//    const C& operator *=   (const C&);
	//    const C& operator /= (const C&);
	//    const C& operator %= (const C&);
	//    const C& operator^=(const C&);
	//    const C& operator&= (const C&);
	//    const C& operator |= (const C&);
	//    const C& operator >>=(const C&);
	//    const C& operator<<= (const C&);
	//    const C& operator<<(const C&);
	//    const C& operator>>(const C&);
	//    const C& operator /**/   == /**/  (const C&);
	//    const C& operator != /**/ (const C&);
	//    const C& operator <= (const C&);
	//    const C& operator /**/ >=(const C&);
	//    const C& operator =(const C&);
	//    const C& operator&& (const C&);
	//    const C& operator ||(const C&);
	//    const C& operator ++(const C&);
	//    const C& operator-- (const C&);
	//    const C& operator/**/,/**/(const C&);
	//    const C& operator->*
	// (const C&);
	//    const C& operator -> (const C&);
	//    const C& operator /**/ ( /**/ ) /**/ (const C&);
	//    const C& operator /**/ [ /**/ ] /**/ (const C&);
	//    const C& operator + (const C&);
	//    const C& operator- (const C&);
	//    const C& operator *(const C&);
	//    const C& operator /(const C&);
	//    const C& operator % /**/(const C&);
	//    const C& operator ^(const C&);
	//    const C& operator &(const C&);
	//    const C& operator |(const C&);
	//    const C& operator   ~ (const C&);
	//    const C& operator 	
	//  ! /**/ (const C&);
	//    const C& operator <(const C&);
	//    const C& operator>(const C&);
	// };                                       
	public void testOperatorNames() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 161);
		assertEquals(col.getName(1).toString(), "operator new[]"); //$NON-NLS-1$
		assertEquals(col.getName(3).toString(), "operator new"); //$NON-NLS-1$
		assertEquals(col.getName(5).toString(), "operator delete[]"); //$NON-NLS-1$
		assertEquals(col.getName(7).toString(), "operator delete"); //$NON-NLS-1$
		assertEquals(col.getName(10).toString(), "operator +="); //$NON-NLS-1$
		assertEquals(col.getName(14).toString(), "operator -="); //$NON-NLS-1$
		assertEquals(col.getName(18).toString(), "operator *="); //$NON-NLS-1$
		assertEquals(col.getName(22).toString(), "operator /="); //$NON-NLS-1$
		assertEquals(col.getName(26).toString(), "operator %="); //$NON-NLS-1$
		assertEquals(col.getName(30).toString(), "operator ^="); //$NON-NLS-1$
		assertEquals(col.getName(34).toString(), "operator &="); //$NON-NLS-1$
		assertEquals(col.getName(38).toString(), "operator |="); //$NON-NLS-1$
		assertEquals(col.getName(42).toString(), "operator >>="); //$NON-NLS-1$
		assertEquals(col.getName(46).toString(), "operator <<="); //$NON-NLS-1$
		assertEquals(col.getName(50).toString(), "operator <<"); //$NON-NLS-1$
		assertEquals(col.getName(54).toString(), "operator >>"); //$NON-NLS-1$
		assertEquals(col.getName(58).toString(), "operator =="); //$NON-NLS-1$
		assertEquals(col.getName(62).toString(), "operator !="); //$NON-NLS-1$
		assertEquals(col.getName(66).toString(), "operator <="); //$NON-NLS-1$
		assertEquals(col.getName(70).toString(), "operator >="); //$NON-NLS-1$
		assertEquals(col.getName(74).toString(), "operator ="); //$NON-NLS-1$
		assertEquals(col.getName(78).toString(), "operator &&"); //$NON-NLS-1$
		assertEquals(col.getName(82).toString(), "operator ||"); //$NON-NLS-1$
		assertEquals(col.getName(86).toString(), "operator ++"); //$NON-NLS-1$
		assertEquals(col.getName(90).toString(), "operator --"); //$NON-NLS-1$
		assertEquals(col.getName(94).toString(), "operator ,"); //$NON-NLS-1$
		assertEquals(col.getName(98).toString(), "operator ->*"); //$NON-NLS-1$
		assertEquals(col.getName(102).toString(), "operator ->"); //$NON-NLS-1$
		assertEquals(col.getName(106).toString(), "operator ()"); //$NON-NLS-1$
		assertEquals(col.getName(110).toString(), "operator []"); //$NON-NLS-1$
		assertEquals(col.getName(114).toString(), "operator +"); //$NON-NLS-1$
		assertEquals(col.getName(118).toString(), "operator -"); //$NON-NLS-1$
		assertEquals(col.getName(122).toString(), "operator *"); //$NON-NLS-1$
		assertEquals(col.getName(126).toString(), "operator /"); //$NON-NLS-1$
		assertEquals(col.getName(130).toString(), "operator %"); //$NON-NLS-1$
		assertEquals(col.getName(134).toString(), "operator ^"); //$NON-NLS-1$
		assertEquals(col.getName(138).toString(), "operator &"); //$NON-NLS-1$
		assertEquals(col.getName(142).toString(), "operator |"); //$NON-NLS-1$
		assertEquals(col.getName(146).toString(), "operator ~"); //$NON-NLS-1$
		assertEquals(col.getName(150).toString(), "operator !"); //$NON-NLS-1$
		assertEquals(col.getName(154).toString(), "operator <"); //$NON-NLS-1$
		assertEquals(col.getName(158).toString(), "operator >"); //$NON-NLS-1$
	}
	
	// typedef int I;             
	// typedef int I;             
	// typedef I I;               
	// class A {                  
	//    typedef char I;         
	//    typedef char I;         
	//    typedef I I;            
	// };                         
	public void testBug90623() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ITypedef I1 = (ITypedef) col.getName(0).resolveBinding();
		ITypedef I2 = (ITypedef) col.getName(1).resolveBinding();
		ITypedef I3 = (ITypedef) col.getName(2).resolveBinding();
		ITypedef I4 = (ITypedef) col.getName(3).resolveBinding();
		ITypedef I8 = (ITypedef) col.getName(5).resolveBinding();
		ITypedef I5 = (ITypedef) col.getName(8).resolveBinding();
		ITypedef I6 = (ITypedef) col.getName(7).resolveBinding();
		ITypedef I7 = (ITypedef) col.getName(6).resolveBinding();
		// ITypedef I8 = (ITypedef) col.getName(5).resolveBinding();
		
		assertSame(I1, I2);
		assertSame(I2, I3);
		assertSame(I3, I4);
		assertNotSame(I4, I5);
		assertSame(I5, I6);
		assertSame(I6, I7);
		assertSame(I7, I8);
		
		assertTrue(I1.getType() instanceof IBasicType);
		assertEquals(((IBasicType) I1.getType()).getType(), IBasicType.t_int);
		
		assertTrue(I8.getType() instanceof IBasicType);
		assertEquals(((IBasicType) I8.getType()).getType(), IBasicType.t_char);
	}
	
	// typedef int I;             
	// void f11( I i );           
	// void main(){ f a; }          
	public void testBug90623_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IASTName f = col.getName(5);
		f.getCompletionContext().findBindings(f, true);
	}
	
	// class X {            
	//    X( const X & );   
	// };                   
	// class Y {            
	//    operator X ();    
	// };                   
	// Y y;                 
	// X x = new X( y );    
	public void testBug90654_1() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPConstructor ctor1 = (ICPPConstructor) col.getName(1)
		.resolveBinding();
		ICPPConstructor ctor = (ICPPConstructor) col.getName(11)
		.resolveBinding();
		assertSame(ctor, ctor1);
	}
	
	// struct A {                
	//    operator short();      
	// } a;                      
	// int f( int );             
	// int f( float );           
	// int x = f(a);             
	public void testBug90654_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(3).resolveBinding();
		IFunction f2 = (IFunction) col.getName(8).resolveBinding();
		assertSame(f1, f2);
	}
	
	// struct A {};                    
	// struct B : public A {           
	//    B& operator = (const B & );  
	// };                              
	// B& B::operator = (const B & s){ 
	//    this->A::operator=(s);       
	//    return *this;                
	// }                               
	public void testBug90653() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPMethod implicit = A.getMethods()[2];
		
		ICPPMethod op1 = (ICPPMethod) col.getName(4).resolveBinding();
		ICPPMethod op2 = (ICPPMethod) col.getName(10).resolveBinding();
		
		ICPPMethod op = (ICPPMethod) col.getName(15).resolveBinding();
		
		assertSame(op1, op2);
		assertSame(op, implicit);
	}
	
	// void f( char * );            
	// void foo() {                 
	//    f( "test" );            
	// }                            
	public void testBug86618() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(0).resolveBinding();
		assertInstances(col, f, 2);
	}
	
	// void f( int (*pf) (char) );       
	// int g( char );                    
	// void foo () {                     
	//    f( g ) ;                       
	// }                                 
	public void testBug45129() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction f1 = (ICPPFunction) col.getName(0).resolveBinding();
		ICPPFunction g1 = (ICPPFunction) col.getName(3).resolveBinding();
		
		IBinding f2 = col.getName(6).resolveBinding();
		IBinding g2 = col.getName(7).resolveBinding();
		
		assertSame(f1, f2);
		assertSame(g1, g2);
	}
	
	// class ABC { 
	//    class DEF { };
	//    static int GHI;
	// }; 
	// int ABC::GHI = 77; // ray bourque
	// int f() { 
	//   int value;
	//   ABC::DEF * var;
	//   ABC::GHI * value;
	// }
	public void testAmbiguousStatements() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTDeclaration[] declarations = tu.getDeclarations();
		assertEquals(3, declarations.length);
		IASTCompoundStatement cs = (IASTCompoundStatement) ((IASTFunctionDefinition) declarations[2])
		.getBody();
		assertTrue(cs.getStatements()[1] instanceof IASTDeclarationStatement);
		assertTrue(cs.getStatements()[2] instanceof IASTExpressionStatement);
		
	}
	
	// void f(){                              
	//    union { int a; char* p; };          
	//    a = 1;                              
	// }                                      
	public void testBug86639() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField a = (ICPPField) col.getName(2).resolveBinding();
		ICPPField a2 = (ICPPField) col.getName(4).resolveBinding();
		assertSame(a, a2);
	}
	
	// void f () {                     
	//    int aa1, aa2;                
	//    a;                           
	// }                               
	public void testBug80940() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IVariable a1 = (IVariable) col.getName(1).resolveBinding();
		IVariable a2 = (IVariable) col.getName(2).resolveBinding();
		
		IBinding[] bs = col.getName(3).getCompletionContext().findBindings(
				col.getName(3), true);
		assertEquals(bs.length, 2);
		assertSame(bs[0], a1);
		assertSame(bs[1], a2);
	}
	
	// struct Ex {                            
	//    int d();                            
	//    int d() const;                      
	// };                                     
	// int Ex::d() {}                         
	// int Ex::d() const {}                   
	// void f() {                             
	//    const Ex * e;                       
	//    e->d();                             
	// }                                      
	public void testBug77024() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction d1 = (ICPPFunction) col.getName(1).resolveBinding();
		ICPPFunction d2 = (ICPPFunction) col.getName(2).resolveBinding();
		
		assertNotSame(d1, d2);
		
		assertFalse(d1.getType().isConst());
		assertTrue((d2.getType()).isConst());
		
		ICPPFunction dr1 = (ICPPFunction) col.getName(5).resolveBinding();
		ICPPFunction dr2 = (ICPPFunction) col.getName(8).resolveBinding();
		
		assertSame(d1, dr1);
		assertSame(d2, dr2);
		
		IBinding r = col.getName(13).resolveBinding();
		assertSame(d2, r);
	}
	
	// class Point {                    
	//    Point() : xCoord(0) {}    
	//    int xCoord;               
	// };                           
	public void testBug91773() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField x = (ICPPField) col.getName(2).resolveBinding();
		ICPPField x2 = (ICPPField) col.getName(3).resolveBinding();
		assertSame(x, x2);
	}
	
	public void testBug90648() throws ParserException {
		IASTTranslationUnit tu = parse(
				"int f() { int (&ra)[3] = a; }", ParserLanguage.CPP); //$NON-NLS-1$
		IASTFunctionDefinition f = (IASTFunctionDefinition) tu
		.getDeclarations()[0];
		IASTCompoundStatement body = (IASTCompoundStatement) f.getBody();
		final IASTDeclarationStatement statement = (IASTDeclarationStatement) body
		.getStatements()[0];
		IASTSimpleDeclaration d = (IASTSimpleDeclaration) statement
		.getDeclaration();
		IASTSimpleDeclSpecifier declSpec = (IASTSimpleDeclSpecifier) d
		.getDeclSpecifier();
		assertEquals(IASTSimpleDeclSpecifier.t_int, declSpec.getType());
		final IASTDeclarator[] declarators = d.getDeclarators();
		assertEquals(declarators.length, 1);
		assertTrue(declarators[0] instanceof IASTArrayDeclarator);
	}
	
	public void testBug92980() throws Exception {
		String code = "struct A { A(); A(const A&) throw(1); ~A() throw(X); };"; //$NON-NLS-1$
		parse(code, ParserLanguage.CPP, true, false);
	}
	
	// class Dummy { int v(); int d; };                
	// void Dummy::v( int ){ d++; }                    
	public void testBug92882() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(5).resolveBinding() instanceof IProblemBinding);
		ICPPField d1 = (ICPPField) col.getName(2).resolveBinding();
		ICPPField d2 = (ICPPField) col.getName(7).resolveBinding();
		assertSame(d1, d2);
	}
	
	// void f( int, int );                    
	// void f( int, int = 3);                 
	// void f( int = 2, int );                
	// void g() {                             
	//    f( 3 );                             
	//    f( );                               
	// }                                      
	public void testBug86547() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f1 = (IFunction) col.getName(0).resolveBinding();
		assertInstances(col, f1, 5);
	}
	
	public void testBug90647() throws Exception {
		parse(
				"char msg[] = \"Syntax error on line %s\\n\";", ParserLanguage.CPP); //$NON-NLS-1$
	}
	
	// int main(int argc, char **argv)
	// {
	// int sum=0;
	// int i;
	// for (i = 0; i < 10; ++i)
	// for (int j = 0; j < 3; ++j)
	// sum += j;
	// for (i = 0; i < 10; ++i)
	// for (int j = 0; j < 3; ++j) // line X
	// sum += j;  // line Y
	// int k;
	// k = sum;
	// }
	public void testBug82766() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings(col);
	}
	
	// int main(int argc, char *argv[])
	// {
	//     unsigned long l = 0;
	//     char *c;
	//     l |= ((unsigned long)(*((c)++)))<<24;
	// }
	public void testBug77385() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings(col);
	}
	
	public void testBug83997() throws Exception {
		IASTTranslationUnit tu = parse(
				"namespace { int x; }", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings(col);
	}
	
	protected void assertNoProblemBindings(CPPNameCollector col) {
		Iterator i = col.nameList.iterator();
		while (i.hasNext()) {
			IASTName n = (IASTName) i.next();
			assertFalse(n.resolveBinding() instanceof IProblemBinding);
		}
	}
	
	private void assertProblemBinding(int id, IBinding b) {
		assertTrue(b instanceof IProblemBinding);
		assertEquals(id, ((IProblemBinding) b).getID());
	}

	protected void assertProblemBindings(CPPNameCollector col, int count) {
		Iterator i = col.nameList.iterator();
		int sum = 0;
		while (i.hasNext()) {
			IASTName n = (IASTName) i.next();
			assertNotNull(n.resolveBinding());
			if (n.getBinding() instanceof IProblemBinding)
				++sum;
		}
		assertEquals(count, sum);
	}
	
	public void testBug85786() throws Exception {
		IASTTranslationUnit tu = parse(
				"void f( int ); void foo () { void * p = &f; ( (void (*) (int)) p ) ( 1 ); }", ParserLanguage.C); //$NON-NLS-1$
		CPPNameCollector nameResolver = new CPPNameCollector();
		tu.accept(nameResolver);
		assertNoProblemBindings(nameResolver);
	}
	
	// class C {                   
	//    static const int n = 1;  
	//    static int arr[ n ];     
	// };                          
	// int C::arr[n];              
	public void testBug90610() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField n = (ICPPField) col.getName(1).resolveBinding();
		assertTrue(n.isStatic());
		
		assertInstances(col, n, 3);
	}
	
	// int a;                
	// extern int b;         
	// extern int c = 1;     
	// int f( );             
	// int f( int p ){}      
	// struct S;             
	// struct S { int d; };  
	// struct X {            
	//    static int y;      
	// };                    
	// namespace N {}        
	// int X::y = 1;         
	// int ( *g(int) )(int); 
	// int ( *pf)(int);      
	public void testDeclDefn() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).isDefinition()); // a
		assertFalse(col.getName(1).isDefinition()); // b
		assertTrue(col.getName(2).isDefinition()); // c
		assertFalse(col.getName(3).isDefinition()); // f ()
		assertTrue(col.getName(4).isDefinition()); // f () {}
		assertTrue(col.getName(5).isDefinition()); // p
		assertFalse(col.getName(6).isDefinition()); // struct S;
		assertTrue(col.getName(7).isDefinition()); // struct S {}
		assertTrue(col.getName(8).isDefinition()); // d
		assertTrue(col.getName(9).isDefinition()); // X
		assertFalse(col.getName(10).isDefinition()); // y
		assertTrue(col.getName(11).isDefinition()); // N
		assertTrue(col.getName(12).isDefinition()); // X::y
		assertFalse(col.getName(15).isDefinition()); // g
		assertTrue(col.getName(18).isDefinition()); // pf
	}
	
	// int f(double);                    
	// int f(int);                       
	// int (&rfi)(int) = f;              
	// int (&rfd)(double) = f;           
	public void testBug95200() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction f1 = (ICPPFunction) col.getName(0).resolveBinding();
		ICPPFunction f2 = (ICPPFunction) col.getName(2).resolveBinding();
		
		assertSame(col.getName(6).resolveBinding(), f2);
		assertSame(col.getName(9).resolveBinding(), f1);
	}
	
	public void testBug95425() throws Exception {
		IASTTranslationUnit tu = parse("class A { A(); };", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPConstructor ctor = (ICPPConstructor) col.getName(1)
		.resolveBinding();
		
		ICPPConstructor[] ctors = A.getConstructors();
		assertEquals(2, ctors.length); // one user declared constructor, one copy constructor
		assertSame(ctor, ctors[1]);
		
		tu = parse("class A { A( void ); };", ParserLanguage.CPP); //$NON-NLS-1$
		col = new CPPNameCollector();
		tu.accept(col);
		
		A = (ICPPClassType) col.getName(0).resolveBinding();
		ctor = (ICPPConstructor) col.getName(1).resolveBinding();
		
		ctors = A.getConstructors();
		assertEquals(2, ctors.length); // one user declared constructor, one copy constructor
		assertSame(ctor, ctors[1]);
	}
	
	// void f( char * );                 
	// void g(){                         
	//    char x[100];                   
	//    f( x );                        
	// }                                 
	public void testBug95461() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction f1 = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(f1, col.getName(4).resolveBinding());
	}
	
	// class A { };
	// int f() { 
	//   A * b = 0;
	//   A & c = 0;
	// }
	public void testAmbiguity() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTSimpleDeclaration A = (IASTSimpleDeclaration) tu.getDeclarations()[0];
		IASTFunctionDefinition f = (IASTFunctionDefinition) tu
		.getDeclarations()[1];
		IASTCompoundStatement body = (IASTCompoundStatement) f.getBody();
		for (int i = 0; i < 2; ++i) {
			IASTDeclarationStatement ds = (IASTDeclarationStatement) body
			.getStatements()[i];
			String s1 = ((IASTNamedTypeSpecifier) ((IASTSimpleDeclaration) ds
					.getDeclaration()).getDeclSpecifier()).getName().toString();
			String s2 = ((IASTCompositeTypeSpecifier) A.getDeclSpecifier())
			.getName().toString();
			assertEquals(s1, s2);
		}
	}
	
	// struct A {
	//  int a; 
	// };
	// struct B: virtual A { };
	// struct C: B { };
	// struct D: B { };
	// struct E: public C, public D { };
	// struct F: public A { };
	// void f() {
	// E e;
	// e.B::a = 0;
	// F f;
	// f.A::a = 1;
	// }
	public void testBug84696() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertEquals(col.size(), 26);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		
		assertNotNull(A);
		assertNotNull(B);
		
		assertInstances(col, A, 4);
		assertInstances(col, B, 4);
	}
	
	// class X {
	//   public:
	//   void f(int);
	//   int a;
	// };
	// int X:: * pmi = &X::a;
	public void testBasicPointerToMember() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		assertEquals(tu.getDeclarations().length, 2);
		IASTSimpleDeclaration p2m = (IASTSimpleDeclaration) tu
		.getDeclarations()[1];
		IASTDeclarator d = p2m.getDeclarators()[0];
		ICPPASTPointerToMember po = (ICPPASTPointerToMember) d
		.getPointerOperators()[0];
		assertEquals("X::", po.getName().toString()); //$NON-NLS-1$
	}
	
	// struct B {};
	// struct D : B {};
	// void foo(D* dp)
	// {
	// B* bp = dynamic_cast<B*>(dp);
	// }
	public void testBug84466() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		ICPPASTCastExpression dynamic_cast = (ICPPASTCastExpression) ((IASTInitializerExpression) ((IASTSimpleDeclaration) ((IASTDeclarationStatement) ((IASTCompoundStatement) ((IASTFunctionDefinition) tu
				.getDeclarations()[2]).getBody()).getStatements()[0])
				.getDeclaration()).getDeclarators()[0].getInitializer())
				.getExpression();
		
		assertEquals(dynamic_cast.getOperator(),
				ICPPASTCastExpression.op_dynamic_cast);
	}
	
	public void testBug88338_CPP() throws Exception {
		IASTTranslationUnit tu = parse(
				"struct A; struct A* a;", ParserLanguage.CPP); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).isDeclaration());
		assertFalse(col.getName(0).isReference());
		assertTrue(col.getName(1).isReference());
		assertFalse(col.getName(1).isDeclaration());
		
		tu = parse("struct A* a;", ParserLanguage.CPP); //$NON-NLS-1$
		col = new CPPNameCollector();
		tu.accept(col);
		
		assertTrue(col.getName(0).isDeclaration());
		assertFalse(col.getName(0).isReference());
	}
	
	public void testPointerToFunction_CPP() throws Exception {
		IASTTranslationUnit tu = parse("int (*pfi)();", ParserLanguage.CPP); //$NON-NLS-1$
		assertEquals(tu.getDeclarations().length, 1);
		IASTSimpleDeclaration d = (IASTSimpleDeclaration) tu.getDeclarations()[0];
		assertEquals(d.getDeclarators().length, 1);
		IASTStandardFunctionDeclarator f = (IASTStandardFunctionDeclarator) d
		.getDeclarators()[0];
		assertEquals(f.getName().toString(), ""); //$NON-NLS-1$
		assertNotNull(f.getNestedDeclarator());
		assertEquals(f.getNestedDeclarator().getName().toString(), "pfi"); //$NON-NLS-1$
	}
	
	// class X { public: int bar; };   
	// void f(){                       
	//    X a[10];                     
	//    a[0].bar;                    
	// }                               
	public void testBug95484() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField bar = (ICPPField) col.getName(1).resolveBinding();
		assertSame(bar, col.getName(6).resolveBinding());
	}
	
	// int strcmp( const char * );          
	// void f( const char * const * argv ){ 
	//    strcmp( *argv );                  
	// }                                    
	public void testBug95419() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction strcmp = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(strcmp, col.getName(4).resolveBinding());
	}
	
	//    class Other;                           
	//    class Base {                           
	//       public: Base( Other * );            
	//    };                                     
	//    class Sub : public Base {              
	//       public: Sub( Other * );             
	//    };                                     
	//    Sub::Sub( Other * b ) : Base(b) {}     
	public void testBug95673() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		
		ICPPConstructor ctor= ba.assertNonProblem("Base( Other", 4, ICPPConstructor.class);
		ICPPConstructor ctor2=  ba.assertNonProblem("Base(b)", 4, ICPPConstructor.class);
		assertSame(ctor, ctor2);
	}
	
	// void mem( void *, const void * );                       
	// void f() {                                              
	//    char *x;  int offset;                                
	//    mem( x, "FUNC" );                                  
	//    mem( x + offset, "FUNC2" );                        
	// }                                                       
	public void testBug95768() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction mem = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(mem, col.getName(6).resolveBinding());
		assertSame(mem, col.getName(8).resolveBinding());
	}
	
	// void trace( const void * );                     
	// class Foo {                                     
	//    public: int import();                        
	// };                                              
	// int Foo::import(){                              
	//    trace( this );                               
	// }                                               
	public void testBug95741() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction trace = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(trace, col.getName(7).resolveBinding());
	}
	
	// class RTCharacter {                        
	//    char value;                             
	//    public: operator char (void) const;     
	// };                                         
	// RTCharacter::operator char( void )const {  
	//    return value;                           
	// }                                          
	public void testBug95692() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPMethod op = (ICPPMethod) col.getName(2).resolveBinding();
		assertSame(op, col.getName(6).resolveBinding());
	}
	
	// int str( const char * );               
	// void f(){                              
	//    str( 0 );                           
	//    str( 00 );  str( 0x0 );             
	// }                                      
	public void testBug95734() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction str = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(str, col.getName(3).resolveBinding());
		assertSame(str, col.getName(4).resolveBinding());
		assertSame(str, col.getName(5).resolveBinding());
	}
	
	// int str( bool );                       
	// enum { ONE };                          
	// void f( char * p ){                    
	//    str( 1.2 );                         
	//    str( ONE );  str( p );              
	// }                                      
	public void testBug95734_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction str = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame(str, col.getName(6).resolveBinding());
		assertSame(str, col.getName(7).resolveBinding());
		assertSame(str, col.getName(9).resolveBinding());
	}
	
	// void f() {
	// char * value;
	// ::operator delete(value);
	// }
	public void testBug95786() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertFalse(col.getName(2).resolveBinding() instanceof IProblemBinding);
	}
	
	// int foo()
	// try
	// {
	// // function body
	// }
	// catch (...)
	// {
	// }
	public void testBug86868() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		assertEquals(tu.getDeclarations().length, 1);
	}
	
	// void f( int t ){
	// int s ( t );
	// }
	public void testBug94779() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTDeclarationStatement ds = (IASTDeclarationStatement) ((IASTCompoundStatement) ((IASTFunctionDefinition) tu
				.getDeclarations()[0]).getBody()).getStatements()[0];
		IASTDeclarator d = ((IASTSimpleDeclaration) ds.getDeclaration())
		.getDeclarators()[0];
		assertTrue(d.getName().resolveBinding() instanceof IVariable);
	}
	
	// int t= 0;
	// int s ( t );
	public void testBug211756() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTSimpleDeclaration sd = (IASTSimpleDeclaration) (tu.getDeclarations()[1]);
		IASTDeclarator d = sd.getDeclarators()[0];
		assertTrue(d.getName().resolveBinding() instanceof IVariable);
	}
	
	// typedef struct xs {           
	//    int state;                 
	// } xs;                         
	// void f( xs *ci ) {            
	//    ci->state;                 
	//    (ci - 1)->state;           
	// }                             
	public void testBug95714() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField state = (ICPPField) col.getName(1).resolveBinding();
		assertSame(state, col.getName(7).resolveBinding());
		assertSame(state, col.getName(9).resolveBinding());
	}
	
	// float _Complex x;
	// double _Complex y;
	public void testBug95757() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP, true, true );
		IASTDeclaration[] decls = tu.getDeclarations();
		
		assertTrue(((IGPPASTSimpleDeclSpecifier)((IASTSimpleDeclaration)decls[0]).getDeclSpecifier()).isComplex());
		assertEquals(((IGPPASTSimpleDeclSpecifier)((IASTSimpleDeclaration)decls[0]).getDeclSpecifier()).getType(), IASTSimpleDeclSpecifier.t_float);
		assertTrue(((IGPPASTSimpleDeclSpecifier)((IASTSimpleDeclaration)decls[1]).getDeclSpecifier()).isComplex());
		assertEquals(((IGPPASTSimpleDeclSpecifier)((IASTSimpleDeclaration)decls[1]).getDeclSpecifier()).getType(), IASTSimpleDeclSpecifier.t_double);
	}
	
	// class _A {                         
	//    static int i;                   
	// };                                 
	// typedef _A A;                      
	// void f(){                          
	//    A::i++;                         
	// }                                  
	public void testTypedefQualified() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField i = (ICPPField) col.getName(1).resolveBinding();
		assertSame(i, col.getName(7).resolveBinding());
	}
	
	// int f() {
	//     return 5;
	// }
	// int main() {
	// int a( 5 );
	// int b( f() );
	// return a+b;
	// }
	public void testBug86849() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		assertNoProblemBindings( col );
	}
	
	// void copy( void * );                   
	// typedef struct {} A;                   
	// void f( A * a ) {                      
	//    copy( a );                          
	// }                                      
	public void testBug96655() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction copy = (ICPPFunction) col.getName(0).resolveBinding();
		assertSame( copy, col.getName(7).resolveBinding() );
	}
	
	public void testBug96678() throws Exception {
		parse( "int x; // comment \r\n", ParserLanguage.CPP, false, true ); //$NON-NLS-1$
	}
	
	// struct A {};                      
	// void copy( A * );                 
	// void f( ) {                       
	//    copy( new A() );               
	// }                                 
	public void testNewExpressionType() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPFunction copy = (ICPPFunction) col.getName(1).resolveBinding();
		assertSame( copy, col.getName(5).resolveBinding() );
	}
	
	// class A {                        
	//    A( int i = 0 );               
	// };                               
	public void testDefaultConstructor() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPConstructor ctor = (ICPPConstructor) col.getName(1).resolveBinding();
		
		ICPPConstructor [] cs = A.getConstructors();
		assertTrue( cs.length == 2 );
		assertSame( cs[1], ctor );
	}
	
	// class C { public: int foo; };                 
	// class B {                                     
	//    C* operator ->();                          
	//    C& operator [] ( int );                    
	// };                                            
	// void f(){                                     
	//    B b;                                       
	//    b->foo;                                    
	//    b[0].foo;                                  
	// }                                             
	public void testBug91707() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col); 
		
		ICPPField foo = (ICPPField) col.getName(1).resolveBinding();
		
		assertSame( foo, col.getName(12).resolveBinding() );
		assertSame( foo, col.getName(14).resolveBinding() );
	}
	
	// class A;                    
	// class A {                   
	//    class B;                 
	//    class C {};              
	// };                          
	// class A::B{};               
	public void testBug92425() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col); 
		
		ICPPClassType A = (ICPPClassType) col.getName(0).resolveBinding();
		ICPPClassType B = (ICPPClassType) col.getName(2).resolveBinding();
		ICPPClassType C = (ICPPClassType) col.getName(3).resolveBinding();
		
		ICPPClassType [] classes = A.getNestedClasses();
		assertEquals( classes.length, 2 );
		assertSame( classes[0], B );
		assertSame( classes[1], C );
	}
	
	// namespace A {                                  
	//    struct F {} f;                              
	//    void f( int a) {}                           
	// }                                              
	public void testBug92425_2() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col); 
		
		ICPPNamespace A = (ICPPNamespace) col.getName(0).resolveBinding();
		IBinding [] bindings = A.getMemberBindings();
		assertEquals( bindings.length, 3 );
		assertSame( bindings[0], col.getName(1).resolveBinding() );
		assertSame( bindings[1], col.getName(2).resolveBinding() );
		assertSame( bindings[2], col.getName(3).resolveBinding() );
	}
	
	// A< B< C< D< E< F< G< H<int> > > > > > > > a;  
	// int A::B<int>::* b;                           
	public void testBug98704() throws Exception {
		parse( getAboveComment(), ParserLanguage.CPP );
	}
	
	// void f();          
	// void f( void ) {}  
	public void testBug_AIOOBE() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction f = (IFunction) col.getName(0).resolveBinding();
		assertSame( f, col.getName(1).resolveBinding() );
		IParameter p = (IParameter) col.getName(2).resolveBinding();
		assertNotNull( p );
	}
	
	// void f( const int );     
	// void f( int );           
	// void g() { f(1); }       
	public void testRankingQualificationConversions_a() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertSame( col.getName(2).resolveBinding(), col.getName(5).resolveBinding() );
	}
	
	// void f( const volatile int );
	// void f( const int );
	// void g() { f(1); }
	public void testRankingQualificationConversions_b() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertSame( col.getName(2).resolveBinding(), col.getName(5).resolveBinding() );
	}
	
	// namespace n {                   
	//    namespace m {                
	//       class A;                  
	//    }                            
	// }                               
	// namespace n {                   
	//    namespace m {                
	//       class A { void f(); };    
	//    }                            
	// }                               
	// namespace n {                   
	//    namespace m {                
	//       void A::f(){}             
	//    }                            
	// }                               
	public void testBug98818() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPNamespace n = (ICPPNamespace) col.getName(0).resolveBinding();
		ICPPNamespace m = (ICPPNamespace) col.getName(1).resolveBinding();
		assertSame( n, col.getName(3).resolveBinding() );
		assertSame( n, col.getName(7).resolveBinding() );
		assertSame( m, col.getName(4).resolveBinding() );
		assertSame( m, col.getName(8).resolveBinding() );
		
		ICPPClassType A = (ICPPClassType) col.getName(2).resolveBinding(); 
		assertSame( A, col.getName(5).resolveBinding() );
		
		ICPPMethod f = (ICPPMethod) col.getName(9).resolveBinding();
		assertSame( f, col.getName(11).resolveBinding() );
	}
	
	// struct A {               
	//    struct { int i; } B;  
	//    struct { int j; } C;  
	// };                       
	// void f(){                
	//    A a;                  
	//    a.B.i; a.C.j;         
	// }                        
	public void testAnonymousStructures() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		ICPPField i = (ICPPField) col.getName(12).resolveBinding();
		ICPPField j = (ICPPField) col.getName(15).resolveBinding();
		assertSame( i, col.getName(2).resolveBinding() );
		assertSame( j, col.getName(5).resolveBinding() );
	}
	
	public void testBug99262() throws Exception {
		parse("void foo() {void *f; f=__null;}", ParserLanguage.CPP, true, true); //$NON-NLS-1$
	}
	
	// void f1(int*) {
	// }
	// void f2() {
	//   f1(__null);
	// }
	public void testBug240567() throws Exception {    	  
    	BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		bh.assertNonProblem("f1(__null", 2, ICPPFunction.class);
	}
	
	public void testBug100408() throws Exception {
		IASTTranslationUnit tu = parse( "int foo() { int x=1; (x)*3; }", ParserLanguage.CPP );  //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		assertNoProblemBindings( col );
	}
	
	public void testBug84478_3() throws Exception {
		IASTTranslationUnit tu = parse( "void foo() { switch( int x = 4 ) { case 4: x++; break; default: break;} }", ParserLanguage.CPP ); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		assertNoProblemBindings( col );
		assertSame( col.getName(1).resolveBinding(), col.getName(2).resolveBinding() );
	}
	public void testBug84478_4() throws Exception {
		IASTTranslationUnit tu = parse( "void foo() { for( int i = 0; int j = 0; ++i) {} }", ParserLanguage.CPP ); //$NON-NLS-1$
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		assertNoProblemBindings( col );        
	}
	
	// void f(){                          
	//    if( int x = 1 )  x++;           
	//    else             x--;           
	//    while( int y = 2 )              
	//       y++;                         
	//    for( int a = 1; int b = 2; b++){ 
	//       a++; b++;                    
	//    }                               
	// }                                  
	public void testBug84478_2() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		assertNoProblemBindings( col );      
		IVariable x = (IVariable) col.getName(1).resolveBinding();
		assertSame( x, col.getName(2).resolveBinding() );
		assertSame( x, col.getName(3).resolveBinding() );
		
		IVariable y = (IVariable) col.getName(4).resolveBinding();
		assertSame( y, col.getName(5).resolveBinding() );
		
		IVariable a = (IVariable) col.getName(6).resolveBinding();
		IVariable b = (IVariable) col.getName(7).resolveBinding();
		assertSame( b, col.getName(8).resolveBinding() );
		assertSame( a, col.getName(9).resolveBinding() );
		assertSame( b, col.getName(10).resolveBinding() );
	}
	
	// void free(void*);         
	// void f(char** p) {          
	//    free(p);
	// }                            
	public void testBug100415() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true);
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		IFunction free = (IFunction) col.getName(0).resolveBinding();
		assertSame(free, col.getName(4).resolveBinding());
	}
	
	// class X;                 
	// void f() {               
	//    class A {             
	//       friend class X;    
	//    };                    
	// }                        
	// namespace B {            
	//    class A {             
	//       friend class X;    
	//    };                    
	// }                        
	public void testBug86688() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		ICPPClassType X = (ICPPClassType) col.getName(0).resolveBinding();
		assertNotSame( X, col.getName(3).resolveBinding() );
		assertTrue( col.getName(3).resolveBinding() instanceof ICPPClassType );
		assertSame( X, col.getName(6).resolveBinding() );
	}
	
	// class m {                  
	//    int m::f();             
	// };                         
	// int m::f(){}               
	public void testBug100403() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		ICPPMethod f = (ICPPMethod) col.getName(3).resolveBinding();
		assertSame( f, col.getName(6).resolveBinding() );
	}
	
	// struct A {                      
	//    typedef int AT;              
	//    void f1(AT);                 
	//    void f2(float);              
	// };                              
	// struct B {                      
	//    typedef float BT;            
	//    friend void A::f1(AT);       
	//    friend void A::f2(BT);       
	// };                              
	public void testBug90609() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		ITypedef AT = (ITypedef) col.getName(1).resolveBinding();
		ICPPMethod f1 = (ICPPMethod) col.getName(2).resolveBinding();
		ICPPMethod f2 = (ICPPMethod) col.getName(5).resolveBinding();
		ITypedef BT = (ITypedef) col.getName(8).resolveBinding();
		
		assertSame( f1, col.getName(11).resolveBinding() );
		assertSame( AT, col.getName(12).resolveBinding() );
		assertSame( f2, col.getName(16).resolveBinding() );
		assertSame( BT, col.getName(17).resolveBinding() );
	}
	
	// struct Except { int blah; };       
	// void f() {                         
	//    try { }                         
	//    catch ( Except * e ) {          
	//       e->blah;                     
	//    }                               
	// }                                  
	public void testBug103281() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		IField blah = (IField) col.getName(1).resolveBinding();
		IVariable e = (IVariable) col.getName(4).resolveBinding();
		
		assertSame( e, col.getName(5).resolveBinding() );
		assertSame( blah, col.getName(6).resolveBinding() );
	}
	
	public void testBug78800() throws Exception {
		parseAndCheckBindings( "class Matrix {  public: Matrix & operator *(Matrix &); }; Matrix rotate, translate; Matrix transform = rotate * translate;" ); //$NON-NLS-1$
	}
	
	// struct U { static int i; };      
	// struct V : U { };                
	// struct W : U { using U::i; };    
	// struct X : V, W { void foo(); }; 
	// void X::foo() {                  
	//    i;                            
	// }                                
	public void test10_2s3b() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP, true, true );
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(  col );
		
		ICPPField i = (ICPPField) col.getName(1).resolveBinding();
		ICPPUsingDeclaration using = (ICPPUsingDeclaration) col.getName(6).resolveBinding();
		IBinding[] delegates = using.getDelegates();
		assertEquals( delegates.length, 1 );
		assertSame( i, delegates[0]);
		
		assertSame( i, col.getName(16).resolveBinding() );
	}
	
	// int f() {
	// int x = 4;  while( x < 10 ) blah: ++x;
	// }
	public void test1043290() throws Exception {
		IASTTranslationUnit tu = parseAndCheckBindings(getAboveComment() );
		IASTFunctionDefinition fd = (IASTFunctionDefinition) tu.getDeclarations()[0];
		IASTStatement [] statements = ((IASTCompoundStatement)fd.getBody()).getStatements();
		IASTWhileStatement whileStmt = (IASTWhileStatement) statements[1];
		IASTLabelStatement labelStmt = (IASTLabelStatement) whileStmt.getBody();
		assertTrue( labelStmt.getNestedStatement() instanceof IASTExpressionStatement );
		IASTExpressionStatement es = (IASTExpressionStatement) labelStmt.getNestedStatement();
		assertTrue( es.getExpression() instanceof IASTUnaryExpression );
	}
	
	// int f() { 
	// int i;
	// do { ++i; } while( i < 10 );
	// return 0;
	// }
	public void testBug104800() throws Exception {
		IASTTranslationUnit tu = parseAndCheckBindings( getAboveComment() );
		IASTFunctionDefinition f = (IASTFunctionDefinition) tu.getDeclarations()[0];
		IASTCompoundStatement body = (IASTCompoundStatement) f.getBody();
		assertEquals( body.getStatements().length, 3 );
	}
	
    public void testBug107150() throws Exception {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("#define FUNC_PROTOTYPE_PARAMS(list)    list\r\n"); //$NON-NLS-1$
    	buffer.append("int func1 FUNC_PROTOTYPE_PARAMS((int arg1)){\r\n"); //$NON-NLS-1$
    	buffer.append("return 0;\r\n"); //$NON-NLS-1$
    	buffer.append("}\r\n"); //$NON-NLS-1$
    	buffer.append("int func2 FUNC_PROTOTYPE_PARAMS\r\n"); //$NON-NLS-1$
    	buffer.append("((int arg1)){\r\n"); //$NON-NLS-1$
    	buffer.append("return 0;\r\n"); //$NON-NLS-1$
    	buffer.append("}\r\n"); //$NON-NLS-1$
    	IASTTranslationUnit tu = parse(buffer.toString(), ParserLanguage.CPP);
    	assertFalse( tu.getDeclarations()[1] instanceof IASTProblemDeclaration );
    	
    	buffer = new StringBuffer();
    	buffer.append("#define FUNC_PROTOTYPE_PARAMS(list)    list\n"); //$NON-NLS-1$
    	buffer.append("int func1 FUNC_PROTOTYPE_PARAMS((int arg1)){\n"); //$NON-NLS-1$
    	buffer.append("return 0;\n"); //$NON-NLS-1$
    	buffer.append("}\n"); //$NON-NLS-1$
    	buffer.append("int func2 FUNC_PROTOTYPE_PARAMS\n"); //$NON-NLS-1$
    	buffer.append("((int arg1)){\n"); //$NON-NLS-1$
    	buffer.append("return 0;\n"); //$NON-NLS-1$
    	buffer.append("}\n"); //$NON-NLS-1$
    	tu = parse(buffer.toString(), ParserLanguage.CPP);
    	assertFalse( tu.getDeclarations()[1] instanceof IASTProblemDeclaration );
    }
	
	// class __attribute__((visibility("default"))) FooClass
	// {
	// int foo();
	// };
	// int FooClass::foo() {
	// return 0;
	// }
	public void testBug108202() throws Exception {
		parse( getAboveComment(), ParserLanguage.CPP, true, true );
	}
	

	// // Test redundant class specifiers 
	// class MyClass {
	// 	int MyClass::field;
	// 	static int MyClass::static_field;
	// };
	// int MyClass::static_field;
	public void testBug174791() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP, true, true );
		
		// check class
		IASTSimpleDeclaration sd = (IASTSimpleDeclaration) tu.getDeclarations()[0];
		ICPPASTCompositeTypeSpecifier cts = (ICPPASTCompositeTypeSpecifier) sd.getDeclSpecifier();
		
		IASTSimpleDeclaration md = (IASTSimpleDeclaration) cts.getMembers()[0];
		IASTName name = md.getDeclarators()[0].getName();
		IField field = (IField) name.resolveBinding();
		// would crash and/or return wrong result
		assertFalse(field.isStatic());
		assertFalse(field.isExtern());
		assertFalse(field.isAuto());
		assertFalse(field.isRegister());
		
		md = (IASTSimpleDeclaration) cts.getMembers()[1];
		name = md.getDeclarators()[0].getName();
		field = (IField) name.resolveBinding();
		// would crash
		assertTrue(field.isStatic());
		assertFalse(field.isExtern());
		assertFalse(field.isAuto());
		assertFalse(field.isRegister());
		
		// check real static defn
		sd = (IASTSimpleDeclaration) tu.getDeclarations()[1];
		name = sd.getDeclarators()[0].getName();
		field = (IField) name.resolveBinding();
		assertTrue(field.isStatic());
	}
	
	// namespace nsSplit {}
	// namespace nsSplit {
	//    void a();
	// }
	// void nsSplit::a() {
	// }
	public void testBug180979() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP, true, true );
		
		// check class
		IASTFunctionDefinition fd = (IASTFunctionDefinition) tu.getDeclarations()[2];
		IASTDeclarator fdecl= fd.getDeclarator();
		IASTName name= fdecl.getName();
		IBinding binding= name.resolveBinding();
		assertTrue(binding instanceof IFunction);
		assertFalse(binding instanceof IProblemBinding);
	}
	
	// #define _GLIBCXX_VISIBILITY(V) __attribute__ ((__visibility__ (#V)))
	// #define _GLIBCXX_BEGIN_NAMESPACE(X) namespace X _GLIBCXX_VISIBILITY(default) {
	// _GLIBCXX_BEGIN_NAMESPACE(std)
	// } // end the namespace
	public void testBug195701() throws Exception {
		parse( getAboveComment(), ParserLanguage.CPP, true, true );
	}
	
	// class A {                      
	//    double operator*(const A&); 
	// };                             
	// class B : public A {           
	//    double operator*(double);   
	//    using A::operator*;         
	// };                             
	public void testBug178059() throws Exception {
		parse( getAboveComment(), ParserLanguage.CPP, true, true );
	}
	
	// void foo (void *p) throw () ;                                   
	// void bar (void *p) __attribute__ (( __nonnull__(1) ));          
	// void zot (void *p) throw () __attribute__ (( __nonnull__(1) )); 
	public void testBug179712() throws Exception {
		parse( getAboveComment(), ParserLanguage.CPP, true, true );
	}
	
	//	class C {
	//	public:
	//		int i;
	//	};
	//
	//	void problem1(int x) {}
	//	void problem2(const int *x) {}
	//	void problem3(int* x) {}
	//
	//	void nonproblem1(bool x) {}
	//	void problem4(bool* x) {}
	//	void problem5(const bool* x) {}
	//
	//	void f() {
	//		int C::* ptm;
	//
	//		problem1("p");
	//		problem2("p");
	//		problem3("p");
	//		nonproblem1("p");
	//		problem4("p");
	//		problem5("p");
	//
	//		problem1(ptm);
	//		problem2(ptm);
	//		problem3(ptm);
	//		nonproblem1(ptm);
	//		problem4(ptm);
	//		problem5(ptm);
	//	}
	public void testBug214335() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		
		IBinding b00= bh.assertProblem("problem1(\"", 8);
		IBinding b01= bh.assertProblem("problem2(\"", 8);
		IBinding b02= bh.assertProblem("problem3(\"", 8);
		IBinding b03= bh.assertNonProblem("nonproblem1(\"", 11);
		IBinding b04= bh.assertProblem("problem4(\"", 8);
		IBinding b05= bh.assertProblem("problem5(\"", 8);
		
		IBinding b06= bh.assertProblem("problem1(ptm", 8);
		IBinding b07= bh.assertProblem("problem2(ptm", 8);
		IBinding b08= bh.assertProblem("problem3(ptm", 8);
		IBinding b09= bh.assertNonProblem("nonproblem1(ptm", 11);
		IBinding b10= bh.assertProblem("problem4(ptm", 8);
		IBinding b11= bh.assertProblem("problem5(ptm", 8);
		
		assertInstance(b03, ICPPFunction.class);
		assertInstance(b09, ICPPFunction.class);
	}
	
	//	namespace source {
	//		class cls {
	//		};
	//		void fs();
	//		void fs(int a);
	//		
	//	}
	//  void test1() {
	//		source::fs();
	//		source::fs(1);
	//		source::cls c2;
	//  }
	//
	//	using source::cls;
	//	using source::fs;
	//
	//	void test() {
	//		cls c2;
	//		fs();
	//		fs(1);
	//	}
	public void testReferencesOfUsingDecls() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP, true, true );
		
		IASTDeclaration[] decls = tu.getDeclarations();
		ICPPASTNamespaceDefinition nsdef= (ICPPASTNamespaceDefinition) decls[0];
		ICPPASTUsingDeclaration udcl= (ICPPASTUsingDeclaration) decls[2];
		ICPPASTUsingDeclaration udf= (ICPPASTUsingDeclaration) decls[3];
		IASTFunctionDefinition fdef= (IASTFunctionDefinition) decls[4];
		
		IASTDeclaration[] nsdecls= nsdef.getDeclarations();
		ICPPASTCompositeTypeSpecifier cldef= (ICPPASTCompositeTypeSpecifier) ((IASTSimpleDeclaration) nsdecls[0]).getDeclSpecifier();
		ICPPASTFunctionDeclarator fdecl1= (ICPPASTFunctionDeclarator) ((IASTSimpleDeclaration) nsdecls[1]).getDeclarators()[0];
		ICPPASTFunctionDeclarator fdecl2= (ICPPASTFunctionDeclarator) ((IASTSimpleDeclaration) nsdecls[2]).getDeclarators()[0];
		
		IASTStatement[] stmts= ((IASTCompoundStatement) fdef.getBody()).getStatements();
		IASTName clname= ((IASTNamedTypeSpecifier) ((IASTSimpleDeclaration)((IASTDeclarationStatement) stmts[0]).getDeclaration()).getDeclSpecifier()).getName();
		IASTName fnname1= ((IASTIdExpression) ((IASTFunctionCallExpression) ((IASTExpressionStatement) stmts[1]).getExpression()).getFunctionNameExpression()).getName();
		IASTName fnname2= ((IASTIdExpression) ((IASTFunctionCallExpression) ((IASTExpressionStatement) stmts[2]).getExpression()).getFunctionNameExpression()).getName();
		
		// check class
		IBinding b= cldef.getName().resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(2, tu.getDefinitionsInAST(b).length);		// class-def + using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// class-def + using-decl
		
		// check functions
		b= fdecl1.getName().resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(1, tu.getDefinitionsInAST(b).length);		// using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// func-decl + using-decl
		b= fdecl2.getName().resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(1, tu.getDefinitionsInAST(b).length);		// using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// func-decl + using-decl
		
		// check using declaration class
		b= udcl.getName().resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(2, tu.getDefinitionsInAST(b).length);		// class-def + using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// class-def + using-decl
		
		// check using declaration function
		b= udf.getName().resolveBinding();
		assertEquals(5, tu.getReferences(b).length);			// 4 refs + using-decl
		assertEquals(1, tu.getDefinitionsInAST(b).length);		// using-decl
		assertEquals(3, tu.getDeclarationsInAST(b).length);		// using-decl + 2 func-decls
		
		// check class reference
		b= clname.resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(2, tu.getDefinitionsInAST(b).length);		// class-def + using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// class-def + using-decl
		
		// check function references
		b= fnname1.resolveBinding();							
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(1, tu.getDefinitionsInAST(b).length);		// using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// using-decl + func-decl
		b= fnname2.resolveBinding();
		assertEquals(3, tu.getReferences(b).length);			// 2 refs + using-decl
		assertEquals(1, tu.getDefinitionsInAST(b).length);		// using-decl
		assertEquals(2, tu.getDeclarationsInAST(b).length);		// using-decl + func-decl
	}
	
	// namespace x {
	//    int a;
	// }
	// using namespace x;
	// class O {
	//    class I {
	//       void f();
	//    };
	// };
	// void O::I::f() {
	//    a=0;
	// }
	public void testUsingDirectiveWithNestedClass_Bug209582() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		
		IBinding b= bh.assertNonProblem("a=", 1);
		assertEquals("x", b.getScope().getScopeName().toString());
	}
	
	// class Test {
	// public:
	//    Test(int, int *) {}
	//    Test(int *) {}
	// };
	// void test () {
	//    int bar= 0;
	//    int * pBar = &bar;
	//    Test foo1 (bar, &bar);
	//    Test foo2 (bar, pBar);
	//    Test foo3 (&bar); 
	// }   
	public void testCastAmbiguity_Bug211756() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		
		bh.assertNonProblem("foo1", 4);
		bh.assertNonProblem("foo2", 4);
		bh.assertNonProblem("foo3", 4);
	}
	
	// int foo2() {
	//   int relayIndex = -1;
	//   int numRelays = 0;
	//	 if( relayIndex < 0 || relayIndex > numRelays )
	//	    return 0;
	// }
	public void testTemplateIDAmbiguity_Bug104706() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		
		bh.assertNonProblem("relayIndex <", 10);
		bh.assertNonProblem("relayIndex >", 10);
		bh.assertNonProblem("numRelays )", 9);
	}

	//	template<typename A, typename B>
	//	struct and_ : public integral_constant<bool, (A::value && B::value)> {
	//	};
	//	template<typename A, typename B>
	//	struct or_ : public integral_constant<bool, (A::value || B::value)> {
	//	};
	//	template <class T> struct is_pod
	//	 : integral_constant<bool, (is_integral<T>::value ||
	//	                            is_floating_point<T>::value ||
	//	                            is_pointer<T>::value)> { };
	//
	//	typedef base::integral_constant<bool,
	//	    (base::has_trivial_copy<value_type>::value &&
	//	    base::has_trivial_destructor<value_type>::value)>
	//	    realloc_ok;
	public void testTemplateIDAmbiguity_Bug228118() throws Exception {
		parse(getAboveComment(), ParserLanguage.CPP);
	}

	// namespace ns {
	//    class cl {};
	//    void func(cl c);
	// }
	// using ns::cl;
	// void test() {
	//    ns::cl qualified;
	//    cl unqualified;
	//    func(qualified);
	//    func(unqualified);
	// }
	public void testScopeOfUsingDelegates_Bug219424() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		
		bh.assertNonProblem("cl c", 2);
		bh.assertNonProblem("func(qualified)", 4);
		bh.assertNonProblem("func(unqualified)", 4);
	}
	
	
	// class Test {
	//    void Test::member1();
	//    void Test::member2() {};
	// };
	// void Test::member1(){
	//    member2();
	// }
	public void testQualifiedMemberDeclaration_Bug222026() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		
		IBinding b= bh.assertNonProblem("member1", 7);
		IBinding b2= bh.assertNonProblem("member1(){", 7);
		assertTrue(b instanceof ICPPMethod);
		ICPPMethod m1= (ICPPMethod) b;
		assertEquals("member1", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		bh= new BindingAssertionHelper(code, true);
		b= bh.assertNonProblem("member2", 7);
		b2= bh.assertNonProblem("member2();", 7);
		assertTrue(b instanceof ICPPMethod);
		m1= (ICPPMethod) b;
		assertEquals("member2", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		// different resolution order
		bh= new BindingAssertionHelper(code, true);
		b2= bh.assertNonProblem("member1(){", 7);
		b= bh.assertNonProblem("member1", 7);
		assertTrue(b instanceof ICPPMethod);
		m1= (ICPPMethod) b;
		assertEquals("member1", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		bh= new BindingAssertionHelper(code, true);
		b2= bh.assertNonProblem("member2();", 7);
		b= bh.assertNonProblem("member2", 7);
		assertTrue(b instanceof ICPPMethod);
		m1= (ICPPMethod) b;
		assertEquals("member2", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
	}
	
	// namespace Test {
	//    void Test::member1();
	//    void Test::member2() {};
	// }
	// void Test::member1(){
	//    member2();
	// }
	public void testQualifiedMemberDeclarationInNamespace_Bug222026() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		
		IBinding b= bh.assertNonProblem("member1", 7);
		IBinding b2= bh.assertNonProblem("member1(){", 7);
		assertTrue(b instanceof ICPPFunction);
		ICPPFunction m1= (ICPPFunction) b;
		assertEquals("member1", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		bh= new BindingAssertionHelper(code, true);
		b= bh.assertNonProblem("member2", 7);
		b2= bh.assertNonProblem("member2();", 7);
		assertTrue(b instanceof ICPPFunction);
		m1= (ICPPFunction) b;
		assertEquals("member2", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		// different resolution order
		bh= new BindingAssertionHelper(code, true);
		b2= bh.assertNonProblem("member1(){", 7);
		b= bh.assertNonProblem("member1", 7);
		assertTrue(b instanceof ICPPFunction);
		m1= (ICPPFunction) b;
		assertEquals("member1", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
		
		bh= new BindingAssertionHelper(code, true);
		b2= bh.assertNonProblem("member2();", 7);
		b= bh.assertNonProblem("member2", 7);
		assertTrue(b instanceof ICPPFunction);
		m1= (ICPPFunction) b;
		assertEquals("member2", m1.getName());
		assertEquals("Test", m1.getScope().getScopeName().toString());
		assertSame(b, b2);
	}
	
	// namespace ns { typedef int ns::TINT; } // illegal, still no CCE is expected.
	public void testQualifiedTypedefs_Bug222093() throws Exception{
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		IBinding td= bh.assertProblem("TINT", 4);
		bh.assertProblem("ns::", 2);
	}
	
	// void func() {  
	// int a, b;
	// a < b || (a==b && a < b);
	//   if (a > b) {
	//   }
	// }
	public void testResettingTemplateIdScopesStack_Bug223777() throws Exception{
		parseAndCheckBindings(getAboveComment());
	}
	
	// long x= 10L;
	public void testLongLiteral_225534() throws Exception {
		IASTTranslationUnit tu = parse(getAboveComment(), ParserLanguage.CPP);
		IASTDeclarator decltor= ((IASTSimpleDeclaration)tu.getDeclarations()[0]).getDeclarators()[0];
		IASTInitializerExpression init= (IASTInitializerExpression) decltor.getInitializer();
		ICPPASTLiteralExpression exp= (ICPPASTLiteralExpression) init.getExpression();
		ICPPBasicType type= (ICPPBasicType) exp.getExpressionType();
		assertTrue(type.isLong());
	}
	
	//	void foo/*_a*/(int x) {}
	//	void foo/*_b*/(unsigned int) {}
	//	void foo/*_c*/(short x) {}
	//	void foo/*_d*/(unsigned short x) {}
	//	void foo/*_e*/(long x) {}
	//	void foo/*_f*/(unsigned long x) {}
	//	void foo/*_g*/(long long x) {}
	//	void foo/*_h*/(unsigned long long x) {}
	//	void foo/*_i*/(float x) {}
	//	void foo/*_j*/(double x) {}
	//	void foo/*_k*/(long double x) {}
	//
	//	int main() {
	//		foo/*a1*/(1);
	//		foo/*a2*/(010);
	//		foo/*a3*/(0x010);
	//
	//		foo/*b1*/(1U);
	//		foo/*b2*/(010U);
	//		foo/*b3*/(0x010U);
	//
	//      /*c*/ /*d*/
	// 
	//		foo/*e1*/(1L);
	//		foo/*e2*/(010L);
	//		foo/*e3*/(0x010L);
	//
	//		foo/*f1*/(1UL);
	//		foo/*f2*/(010UL);
	//		foo/*f3*/(0x010UL);
	//
	//		foo/*g1*/(100000000000000000LL);
	//		foo/*g2*/(0100000000000LL);
	//		foo/*g3*/(0x01000000000LL);
	//
	//		foo/*h1*/(100000000000000000ULL);
	//		foo/*h2*/(0100000000000ULL);
	//		foo/*h3*/(0x01000000000ULL);
	//
	//		foo/*i1*/(11.1F);
	//		foo/*i2*/(11E1F);
	//		
	//		foo/*j1*/(11.1);
	//		foo/*j2*/(11.1E1);
	//		
	//		foo/*k1*/(11.1L);
	//		foo/*k2*/(11.1E1L);
	//	}
	public void testLiteralsViaOverloads_225534() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		char[] cs= {'a','b','e','f','g','h','i','j','k'};
		for(char c : cs) {
			for(int i=1; i<(c < 'i' ? 4 : 3); i++) {
				ICPPFunction def= ba.assertNonProblem("foo/*_"+c+"*/", 3, ICPPFunction.class);
				ICPPFunction ref= ba.assertNonProblem("foo/*"+c+""+i+"*/", 3, ICPPFunction.class);
				assertSame("function ref: "+c+""+i, def, ref);
			}
		}
	}
	
	//	class A {
	//		public:
	//			void doA() {}
	//	};
	//
	//	class FirstLevelProxy {
	//		public:
	//			A* operator->() {A *a = new A(); return a;} // leaky
	//			void doFLP() {}
	//	};
	//
	//	class SecondLevelProxy {
	//		public:
	//			FirstLevelProxy operator->() {FirstLevelProxy p; return p;}
	//			void doSLP() {}
	//	};
	//
	//	int main(int argc, char **argv) {
	//		SecondLevelProxy p2;
	//		p2->doA();
	//	}
	public void testRecursiveUserDefinedFieldAccess_Bug205964() throws Exception {
		parseAndCheckBindings(getAboveComment());
	}

	// namespace ns {
	// class A {};
	// }
	//
	// using ns::A;
	//
	// class B: public A {};
	public void testBug235196() throws Exception {
		parseAndCheckBindings(getAboveComment());
	}
	
	// typedef int tint;
	// class X {
	//   typedef int xtint;
	//	 X();
	//	 ~X();
	//	 operator int ();
	//	 tint(a); // 1
	// };
	// X::X() {}
	// X::~X() {}
	// X::operator int() {}
	// X::xtint(a); // 2
	public void testEmptyDeclSpecifier() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("X {", 1, ICPPClassType.class);
		ba.assertNonProblem("X()", 1, ICPPConstructor.class);
		ba.assertNonProblem("~X", 2, ICPPMethod.class);
		ba.assertNonProblem("operator int", 12, ICPPMethod.class);
		ba.assertNonProblem("a); // 1", 1, ICPPField.class);

		ba.assertNonProblem("X() {}", 1, ICPPConstructor.class);
		ba.assertNonProblem("~X() {}", 2, ICPPMethod.class);
		ba.assertNonProblem("operator int() {}", 12, ICPPMethod.class);
		ba.assertNonProblem("a); // 2", 1, ICPPVariable.class);
	}

	// void* operator new (unsigned int, int[100]);
	// typedef int T;
	// int p[100]; 
	// void test(int f) {
	//	  new T; 
	//    new T();
	//    new T(f);
	//    new (p) T; 
	//    new (p) T();
	//    new (p) T(f);
	//	  new (T); 
	//    new (T)();
	//    new (T)(f);
	//    new (p) (T); 
	//    new (p) (T)();
	//    new (p) (T)(f);
	//	  new T[f][f]; 
	//    new (p) T[f][f]; 
	//	  new (T[f][f]); 
	//    new (p) (T[f][f]); 
	// };
	public void testNewPlacement() throws Exception {
		IASTTranslationUnit tu= parseAndCheckBindings(getAboveComment());
		IASTFunctionDefinition fdef= getDeclaration(tu, 3);
		
		checkNewExpression(fdef, 0, null, "int", null);
		checkNewExpression(fdef, 1, null, "int", IASTExpressionList.class);
		checkNewExpression(fdef, 2, null, "int", IASTIdExpression.class);
		checkNewExpression(fdef, 3, IASTIdExpression.class, "int", null);
		checkNewExpression(fdef, 4, IASTIdExpression.class, "int", IASTExpressionList.class);
		checkNewExpression(fdef, 5, IASTIdExpression.class, "int", IASTIdExpression.class);
		checkNewExpression(fdef, 6, null, "int", null);
		checkNewExpression(fdef, 7, null, "int", IASTExpressionList.class);
		checkNewExpression(fdef, 8, null, "int", IASTIdExpression.class);
		checkNewExpression(fdef, 9, IASTIdExpression.class, "int", null);
		checkNewExpression(fdef, 10, IASTIdExpression.class, "int", IASTExpressionList.class);
		checkNewExpression(fdef, 11, IASTIdExpression.class, "int", IASTIdExpression.class);

		checkNewExpression(fdef, 12, null, "int [] []", null);
		checkNewExpression(fdef, 13, IASTIdExpression.class, "int [] []", null);
		checkNewExpression(fdef, 14, null, "int [] []", null);
		checkNewExpression(fdef, 15, IASTIdExpression.class, "int [] []", null);
	}

	private void checkNewExpression(IASTFunctionDefinition fdef, int i_expr, Class<?> placement, String type, Class<?> init) {
		IASTExpression expr;
		ICPPASTNewExpression newExpr;
		expr= getExpressionOfStatement(fdef, i_expr);
		assertInstance(expr, ICPPASTNewExpression.class);
		newExpr= (ICPPASTNewExpression) expr;
		if (placement == null) {
			assertNull(newExpr.getNewPlacement());
		} else {
			assertInstance(newExpr.getNewPlacement(), placement);
		}
		if (init == null) {
			assertNull(newExpr.getNewInitializer());
		} else {
			assertInstance(newExpr.getNewInitializer(), init);
		}
		isTypeEqual(CPPVisitor.createType(newExpr.getTypeId()), type);
	}
	
    // namespace ns {
    //   void test() {}
    //   +
    // }
    public void testTrailingSyntaxErrorInNamespace() throws Exception {
    	final String comment= getAboveComment();
    	IASTTranslationUnit tu= parse(comment, ParserLanguage.CPP, false, false);
    	ICPPASTNamespaceDefinition ns= getDeclaration(tu, 0);
		IASTDeclaration decl= getDeclaration(ns, 0);
		IASTProblemDeclaration pdecl= getDeclaration(ns, 1);
		assertEquals("+", pdecl.getRawSignature());
    }

    // extern "C" {
    //   void test() {}
    //   +
    // }
    public void testTrailingSyntaxErrorInLinkageSpec() throws Exception {
    	final String comment= getAboveComment();
    	IASTTranslationUnit tu= parse(comment, ParserLanguage.CPP, false, false);
    	ICPPASTLinkageSpecification ls= getDeclaration(tu, 0);
		IASTDeclaration decl= getDeclaration(ls, 0);
		IASTProblemDeclaration pdecl= getDeclaration(ls, 1);
		assertEquals("+", pdecl.getRawSignature());
    }
    
    // class C;
    // void func(void (C::*m)(int) const);
    public void test233889_a() throws Exception {
    	BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction func= bh.assertNonProblem("func(", 4, ICPPFunction.class);
		assertEquals(1,func.getParameters().length);
		IType type= func.getParameters()[0].getType();
		ICPPPointerToMemberType ptm= assertInstance(type, ICPPPointerToMemberType.class);
		ICPPFunctionType t= ((ICPPFunctionType)ptm.getType());
		assertTrue(t.isConst());
    }
    
	//	 struct C {
	//		 int m1(int a);
	//		 int m2(int a) const;
	//	 }; 
	//
	//	 C* func(int (C::*m)(int) const);
	//	 C* func(int (C::*m)(int));
	//
	//	 void ref() {
	//		 func(&C::m1);
	//		 func(&C::m2);
	//	 }
    public void testBug233889_b() throws Exception {
		BindingAssertionHelper bh= new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction fn1= bh.assertNonProblem("func(&C::m1", 4, ICPPFunction.class);
		ICPPFunction fn2= bh.assertNonProblem("func(&C::m2", 4, ICPPFunction.class);
		assertNotSame(fn1, fn2);
    }
    
    //	class A;
	//
	//	void foo(A* a) {}
	//	void foo(const A* a) {}
	//	void foo(volatile A* a) {}
	//	void foo(const volatile A* a) {}
	//
	//	class A {
	//	public:
	//		void member1() { foo(this);/*1*/ }
	//		void member2() const { foo(this);/*2*/ }
	//		void member3() volatile { foo(this);/*3*/ }
	//		void member4() const volatile { foo(this);/*4*/ }
	//	};
	public void testThisType() throws Exception {
		BindingAssertionHelper ba=new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction pt1= ba.assertNonProblem("foo(this);/*1*/", 3, ICPPFunction.class);
		ICPPFunction pt2= ba.assertNonProblem("foo(this);/*2*/", 3, ICPPFunction.class);
		ICPPFunction pt3= ba.assertNonProblem("foo(this);/*3*/", 3, ICPPFunction.class);
		ICPPFunction pt4= ba.assertNonProblem("foo(this);/*4*/", 3, ICPPFunction.class);
		
		IType t1= ((IPointerType)pt1.getType().getParameterTypes()[0]).getType();
		IQualifierType t2= (IQualifierType) ((IPointerType) pt2.getType().getParameterTypes()[0]).getType();
		IQualifierType t3= (IQualifierType)((IPointerType) pt3.getType().getParameterTypes()[0]).getType();
		IQualifierType t4= (IQualifierType)((IPointerType) pt4.getType().getParameterTypes()[0]).getType();
		
		assertTrue(!(t1 instanceof IQualifierType));
		assertTrue(t2.isConst()); assertTrue(!t2.isVolatile());
		assertTrue(!t3.isConst()); assertTrue(t3.isVolatile());
		assertTrue(t4.isConst()); assertTrue(t4.isVolatile());
	}
	
	//	class A {
	//		public:
	//			void foo() {}
	//	};
	//
	//	void ref() {
	//		A a1= *new A();
	//		a1->foo();/*1*/
	//      A* a2= new A();
	//      a2->foo();/*2*/
	//	}
	public void testMemberAccessOperator_a() throws Exception {
		BindingAssertionHelper ba=new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("foo();/*1*/", 3);
		ba.assertNonProblem("foo();/*2*/", 3);
	}
	
	//	class A {
	//		public:
	//			void foo() {};
	//	};
	//
	//	class B {
	//		public:
	//			void foo() {};
	//			A* operator->() {return new A();}
	//	};
	//
	//	void ref() {
	//		B b= *new B();
	//		b->foo();/*1*/
	//		B* b2= new B();
	//		b2->foo();/*2*/
	//	}
	public void testMemberAccessOperator_b() throws Exception {
		BindingAssertionHelper ba=new BindingAssertionHelper(getAboveComment(), true);
		ICPPMethod m1= ba.assertNonProblem("foo();/*1*/", 3, ICPPMethod.class);
		ICPPMethod m2= ba.assertNonProblem("foo();/*2*/", 3, ICPPMethod.class);
		assertEquals(m1.getClassOwner().getName(), "A");
		assertEquals(m2.getClassOwner().getName(), "B");
	}
	
	//	class A { public: void foo(); };
	//	class B { public: A* operator->() {return  new A();} };
	//	class C { public: B  operator->() {return *new B();} };
	//
	//	void ref() {
	//	   C c;
	//	   c->foo();/**/ // refers to A::foo
	//	}
	public void testMemberAccessOperator_c() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("foo();/**/", 3);
	}
	
	//	class A { public: void foo(); };
	//	class B { public: A* operator->() {return  new A();} };
	//	class C { public: B* operator->() {return new B();} };
	//
	//	void ref() {
	//	   C c;
	//	   c->foo();/**/ // expect problem - foo is not in B
	//	}
	public void testMemberAccessOperator_d() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("foo();/**/", 3);
	}
	
	//	class A { public: void foo(); };
	//  typedef A A2;
	//	class B { public: A2* operator->() {return  new A();} };
	//  typedef B B2;
	//	class C { public: B2 operator->() {return *new B();} };
	//
	//  typedef C C2;
	//	void ref() {
	//	   C2 c;
	//	   c->foo();/**/ // refers to A::foo
	//	}
	public void testMemberAccessOperator_e() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("foo();/**/", 3);
	}
	
	//  namespace std {class type_info{public: const char* name() const;};}
	//	int main() {
	//		int s;
	//		typeid(int).name();
	//		typeid(s).name();
	//	}
	public void testTypeid_Bug209578() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
	}

	//	typedef int int5[5];
	//
	//	void f1(int* x);
	//	void f2(int** x);
	//	void f3(int5 x);
	//	void f4(int5* x);
	//
	//	void test(int5 p, int5* q, int* r, int** s) {
	//	  f1(p);
	//	  f2(q);
	//	  f3(r);
	//	  f4(s);
	//	}
	public void testArrayToPointerConversion() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("f1(p)", 2, ICPPFunction.class);
		ba.assertProblem("f2(q)", 2);
		ba.assertNonProblem("f3(r)", 2, ICPPFunction.class);
		ba.assertProblem("f4(s)", 2);
	}

	//	typedef char* t1;
	//	typedef char t2[8];
	//  typedef char* charp;
	//	void test(charp x) {}
	//	int main(void) {  
	//		char x[12];
	//		t1 y;
	//		t2 z;
	//		test(x);
	//		test(y);
	//		test(z);
	//	}
	public void testArrayToPtrConversionForTypedefs_Bug239931() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
	}

	//	typedef char t[12];
	//	void test1(char *);
	//	void test2(char []);
	//	void test3(t);
	//	void xx() {
	//	   char* x= 0;
	//	   test1(x);
	//	   test2(x); // problem binding here
	//	   test3(x); // problem binding here
	//	}
	public void testAdjustmentOfParameterTypes_Bug239975() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
	}

	//	class A {
	//		public:
	//			void m(int c);
	//	};
	//
	//	void test(char c) {
	//		void (A::* ptr2mem)(char);
	//		ptr2mem= reinterpret_cast<void (A::*)(char)>(&A::m);
	//      ptr2mem= (void (A::*)(int))(0); 
	//	}
	public void testTypeIdForPtrToMember_Bug242197() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
	}
	
	// void restrict();
	public void testRestrictIsNoCPPKeyword_Bug228826() throws Exception {
		final String code = getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP, false);
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP, true);  // even with gnu extensions
	}
	
	// void test1();
	// void test2() throw ();
	// void test3() throw (int);
	public void testEmptyExceptionSpecification_Bug86943() throws Exception {
		IASTTranslationUnit tu= parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);

		IASTSimpleDeclaration d= getDeclaration(tu, 0);
		ICPPASTFunctionDeclarator fdtor= (ICPPASTFunctionDeclarator) d.getDeclarators()[0];
		IASTTypeId[] specs= fdtor.getExceptionSpecification();
		assertEquals(0, specs.length);
		assertSame(ICPPASTFunctionDeclarator.NO_EXCEPTION_SPECIFICATION, specs);

		d= getDeclaration(tu, 1);
		fdtor= (ICPPASTFunctionDeclarator) d.getDeclarators()[0];
		specs= fdtor.getExceptionSpecification();
		assertEquals(0, specs.length);
		assertNotSame(ICPPASTFunctionDeclarator.NO_EXCEPTION_SPECIFICATION, specs);

		d= getDeclaration(tu, 2);
		fdtor= (ICPPASTFunctionDeclarator) d.getDeclarators()[0];
		specs= fdtor.getExceptionSpecification();
		assertEquals(1, specs.length);
	}
	
	//	int test() {
	//		try {
	//		} catch (const int &ex) {
	//		} catch (const char &ex) {
	//		}
	//	}
	public void testScopeOfCatchHandler_Bug209579() throws Exception {
		parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
	}

	//	void func(const char& c);
	//	void func(char& c);
	//
	//	void test(const char& x, char& y) {
	//	  func(x);
	//	  func(y);
	//	}
	public void testOverloadedFunction_248774() throws Exception {
		BindingAssertionHelper helper= new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction func1= helper.assertNonProblem("func(x)", 4, ICPPFunction.class);
		ICPPFunction func2= helper.assertNonProblem("func(y)", 4, ICPPFunction.class);
		assertNotSame(func1, func2);
	}

	//	struct A {
	//	  const char& operator[](int pos) const;
	//	  char& operator[](int pos);
	//	};
	//	
	//	void func(const char& c);
	//	void func(char& c);
	//
	//	void test(const A& x, A& y) {
	//	  func(x[0]);
	//	  func(y[0]);
	//	}
	public void testOverloadedOperator_248803() throws Exception {
		BindingAssertionHelper helper= new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction func1= helper.assertNonProblem("func(x[0])", 4, ICPPFunction.class);
		ICPPFunction func2= helper.assertNonProblem("func(y[0])", 4, ICPPFunction.class);
		assertNotSame(func1, func2);
	}
	
	// class NonVirt {
	//    void m();//0
	// };
	// class C1 : NonVirt {
	//    virtual void m();//1
	// };
	// class C2 : C1 {
	//    void m();//2
	// };
	// class C3 : C2 {
	//    void m(int);
	// };
	// class C4 : C3 {
	//    void m();//4
	// };
	// class C5 : C1 {
	//    void m();//5
	// };	
	public void testOverridden_248846() throws Exception {
		BindingAssertionHelper helper= new BindingAssertionHelper(getAboveComment(), true);
		ICPPMethod m0= helper.assertNonProblem("m();//0", 1, ICPPMethod.class);
		ICPPMethod m1= helper.assertNonProblem("m();//1", 1, ICPPMethod.class);
		ICPPMethod m2= helper.assertNonProblem("m();//2", 1, ICPPMethod.class);
		ICPPMethod m3= helper.assertNonProblem("m(int);", 1, ICPPMethod.class);
		ICPPMethod m4= helper.assertNonProblem("m();//4", 1, ICPPMethod.class);
		ICPPMethod m5= helper.assertNonProblem("m();//5", 1, ICPPMethod.class);
		
		assertFalse(ClassTypeHelper.isVirtual(m0));
		assertFalse(ClassTypeHelper.isVirtual(m3));
		assertTrue(ClassTypeHelper.isVirtual(m1));
		assertTrue(ClassTypeHelper.isVirtual(m2));
		assertTrue(ClassTypeHelper.isVirtual(m4));
		assertTrue(ClassTypeHelper.isVirtual(m5));
		
		assertFalse(ClassTypeHelper.isOverrider(m0, m0));
		assertFalse(ClassTypeHelper.isOverrider(m1, m0));
		assertFalse(ClassTypeHelper.isOverrider(m2, m0));
		assertFalse(ClassTypeHelper.isOverrider(m3, m0));
		assertFalse(ClassTypeHelper.isOverrider(m4, m0));
		assertFalse(ClassTypeHelper.isOverrider(m5, m0));

		assertFalse(ClassTypeHelper.isOverrider(m0, m1));
		assertFalse(ClassTypeHelper.isOverrider(m1, m1));
		assertFalse(ClassTypeHelper.isOverrider(m3, m1));
		assertTrue(ClassTypeHelper.isOverrider(m2, m1));
		assertTrue(ClassTypeHelper.isOverrider(m4, m1));
		assertTrue(ClassTypeHelper.isOverrider(m5, m1));

		assertFalse(ClassTypeHelper.isOverrider(m0, m2));
		assertFalse(ClassTypeHelper.isOverrider(m1, m2));
		assertFalse(ClassTypeHelper.isOverrider(m2, m2));
		assertFalse(ClassTypeHelper.isOverrider(m3, m2));
		assertFalse(ClassTypeHelper.isOverrider(m5, m2));
		assertTrue(ClassTypeHelper.isOverrider(m4, m2));
		
		ICPPMethod[] ors= ClassTypeHelper.findOverridden(m0);
		assertEquals(0, ors.length);
		ors= ClassTypeHelper.findOverridden(m1);
		assertEquals(0, ors.length);
		ors= ClassTypeHelper.findOverridden(m2);
		assertEquals(1, ors.length);
		assertSame(ors[0], m1);
		ors= ClassTypeHelper.findOverridden(m3);
		assertEquals(0, ors.length);
		ors= ClassTypeHelper.findOverridden(m4);
		assertEquals(2, ors.length);
		assertSame(ors[0], m2);
		assertSame(ors[1], m1);
		ors= ClassTypeHelper.findOverridden(m5);
		assertEquals(1, ors.length);
		assertSame(ors[0], m1);
	}

    //  struct A {
    //    int a;
    //  };
    //
    //  void test(A* p) {
    //    p.a; // should not resolve
    //  }
    public void testPointerMemberAccess_245068() throws Exception {
        final String comment= getAboveComment();
        final boolean[] isCpps= {false, true};
        for (boolean isCpp : isCpps) {
            BindingAssertionHelper ba= new BindingAssertionHelper(comment, isCpp);
            ba.assertProblem("a; // should not resolve", 1);
        }
    }

    //	namespace ns {
    //	  template<typename T> class CT {};
    //  }
    //  using ns::CT<int>;
    public void testTemplateIdInUsingDecl_251199() throws Exception {
        parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
    }

	//	namespace ns {
	//	struct A {};
	//	}
	//
	//	struct B {
	//	  operator ns::A(); // problems on operator ns and on A
	//	};
	public void testNamespaceQualifiedOperator_256840() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		bh.assertNonProblem("operator ns::A", 14);
        parseAndCheckBindings(code, ParserLanguage.CPP);
	}

	//	void f();
	//	
	//	void test(int p) {
	//	  f(p);
	//	}
    public void testFunctionExtraArgument() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("f(p)", 1);
    }

    //	void f(...);
	//	
	//	void test(int* p) {
	//	  f(p);
	//	}
    public void testVariadicFunction_2500582() throws Exception {
    	final String comment= getAboveComment();
    	final boolean[] isCpps= {false, true};
    	for (boolean isCpp : isCpps) {
    		BindingAssertionHelper ba= new BindingAssertionHelper(comment, isCpp);
    		ba.assertNonProblem("f(p)", 1, IFunction.class);
    	}
    }

	//  struct Incomplete;
	//
	//  void f(Incomplete* p);
	//  void f(...);
	//
	//  void test() {
	//	  // Should resolve to f(Incomplete*) since 0 can be converted to Incomplete*
	//    f(0);
	//	}
    public void testVariadicFunction_2500583() throws Exception {
    	final String comment= getAboveComment();
    	final boolean[] isCpps= {false, true};
    	for (boolean isCpp : isCpps) {
    		BindingAssertionHelper ba= new BindingAssertionHelper(comment, isCpp);
    		IFunction decl= ba.assertNonProblem("f(Incomplete* p)", 1, IFunction.class);
    		IFunction func= ba.assertNonProblem("f(0)", 1, IFunction.class);
    		assertSame(decl, func);
    	}
    }
    
    //    class MyClass{
    //    public:
    //       int v;
    //       int& operator () (int i){ return v; }
    //       int& operator () (int i, int j){ return v; }
    //    };
    //
    //    int main(MyClass c, int i){
    //      c(i,i)= 0;
    //    	c(i) = 0;
    //    }
    public void testFunctionCallOnLHS_252695() throws Exception {
		final String code = getAboveComment();
		IASTTranslationUnit tu= parseAndCheckBindings(code, ParserLanguage.CPP, true);
		IASTFunctionDefinition fdef= getDeclaration(tu, 1);
		IASTExpressionStatement exstmt= getStatement(fdef, 0);
		assertInstance(exstmt.getExpression(), IASTBinaryExpression.class);
		exstmt= getStatement(fdef, 1);
		assertInstance(exstmt.getExpression(), IASTBinaryExpression.class);
    }
    
    //    template <int E> class A;
    //    class A {};
    //    class A;
    //    class B;
    //    template <int E> class B;
    //    template <int E> class B {};
    public void testInvalidClassRedeclaration_254961() throws Exception {
		final String code = getAboveComment();
		IASTTranslationUnit tu= parse(code, ParserLanguage.CPP, true, false);
		CPPNameCollector nc= new CPPNameCollector();
		tu.accept(nc);
		assertProblemBindings(nc, 4);
		assertProblemBinding(IProblemBinding.SEMANTIC_INVALID_REDEFINITION, nc.getName(2).resolveBinding());
		assertProblemBinding(IProblemBinding.SEMANTIC_INVALID_REDECLARATION, nc.getName(3).resolveBinding());
		assertProblemBinding(IProblemBinding.SEMANTIC_INVALID_REDECLARATION, nc.getName(6).resolveBinding());
		assertProblemBinding(IProblemBinding.SEMANTIC_INVALID_REDEFINITION, nc.getName(8).resolveBinding());
    }
    
    //    struct Foo {
    //        void foo();
    //    };
    //    void Foo::foo(void) {
    //    }
    public void testVoidParamInDefinition_257376() throws Exception {
    	parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
    }
    
    //    namespace ns {
    //    	struct C {
    //    		C(C* c){}
    //    	};
    //    	int a;
    //    }
    //    struct C {
    //    	C(ns::C*){}
    //    };
    //    void testbla() {
    //    	ns::C* cptr= 0;
    //    	C c= C(cptr);
    //    }
    public void testNoKoenigForConstructors() throws Exception {
    	parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
    }

    //    typedef void VOID;
    //    void donothing();
    //    void donothing(VOID){}
    //    void donothing(VOID);
    //    void test() {
    //      donothing(); 
    //    }
    public void testVoidViaTypedef_Bug258694() throws Exception {
    	parseAndCheckBindings(getAboveComment(), ParserLanguage.CPP);
    }

	//	struct A {
	//	  B method(B p, int& a = y) { // B is not defined 
	//	    B b;
    //      int x = y + 1;
    //      return b;
	//	  }
	//	  struct B {};
    //    int& x = y; // y is not defined
    //    int y;
	//	};
    public void testScopeOfClassMember_259460() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("B b", 1, ICPPClassType.class);
		ba.assertProblem("B p", 1);
		ba.assertProblem("B method", 1);
		ba.assertNonProblem("y + 1;", 1, ICPPField.class);
		ba.assertNonProblem("y) {", 1, ICPPField.class);
		ba.assertProblem("y; // y is not defined", 1);
    }

	//  class A {
	//    int method(int a = GREEN) {
	//      return RED;
	//    }
    //    int x = GREEN; // GREEN is not defined
	//    enum Color {
	//      RED, GREEN
	//    };
	//  };
    public void testScopeOfClassMember_259648() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("GREEN)", 5, IEnumerator.class);
		ba.assertNonProblem("RED;", 3, IEnumerator.class);
		ba.assertProblem("GREEN;", 5);
    }

	//  struct A {
	//    int& operator*();
	//    const int& operator*() const;
	//  };
	//  void func(int& p) {}
	//  void func(const int& p) {}
	//
	// 	void test(A& a, const A& b) {
	// 	  func(*a);
	// 	  func(*b);
	// 	}
	public void testSmartPointerReference_259680() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ICPPFunction f1= ba.assertNonProblem("func(*a)", 4, ICPPFunction.class);
		ICPPFunction f2= ba.assertNonProblem("func(*b)", 4, ICPPFunction.class);
		assertNotSame(f1, f2);
	}

	//	struct C {int a;};
	//	void myfunc(C c) {
	//		return c < c.a ||
	//	        (c == c.a && (c<c.a || (c == c.a && (c<c.a || (c == c.a && (c<c.a ||
	//	        (c == c.a && (c<c.a || (c == c.a && (c<c.a || (c == c.a && (c<c.a ||
	//	        (c == c.a && (c<c.a || (c == c.a && (c<c.a || (c == c.a && (c<c.a ||
	//	        (c == c.a && (c<c.a || (c == c.a && (c<c.a || (c == c.a && (c<c.a ||
	//	        (c == c.a && (c<c.a || (c == c.a && (c<c.a || (c == c.a && (c<c.a
	//	        ))))))))))))))))))))))))))))));
	//	}
	public void testNestedTemplateIDAmbiguity_259501() throws Exception {
		final String code= getAboveComment();
		parseAndCheckBindings(code);
	}

    //  struct A {
    //    int a;
    //  };
    //
    //  struct B {
    //    A operator-(B b);
    //    A operator+(B& b);
    //    A operator*(const B& b);
    //    A operator/(B b) const;
    //    A operator%(const B& b) const;
    //  };
    //
    //  void test(B p1, B p2) {
    //    (p1 - p2).a; //1
    //    (p1 + p2).a; //2
    //    (p1 * p2).a; //3
    //    (p1 / p2).a; //4
    //    (p1 % p2).a; //5
    //  }
    public void testOverloadedBinaryOperator_259927_1() throws Exception {
        BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
        ba.assertNonProblem("a; //1", 1, ICPPField.class);
        ba.assertNonProblem("a; //2", 1, ICPPField.class);
        ba.assertNonProblem("a; //3", 1, ICPPField.class);
        ba.assertNonProblem("a; //4", 1, ICPPField.class);
        ba.assertNonProblem("a; //5", 1, ICPPField.class);
    }

    //  struct A {
    //    int a;
    //  };
    //  struct B {};
    //  enum E { zero };
    //
    //  A operator-(B p1, int p2);
    //  A operator+(int p1, const B& p2);
    //  A operator*(E p1, int p2);
    //  A operator/(int p1, const E& p2);
    //  A operator%(const B& p1, const B& p2);
    //
    //  void test(B b, E e, int i) {
    //    (b - i).a; //1
    //    (i + b).a; //2
    //    (e * i).a; //3
    //    (i / e).a; //4
    //    (b % b).a; //5
    //    (b + i).a; //6
    //  }
    public void testOverloadedBinaryOperator_259927_2() throws Exception {
        BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
        ba.assertNonProblem("a; //1", 1, ICPPField.class);
        ba.assertNonProblem("a; //2", 1, ICPPField.class);
        ba.assertNonProblem("a; //3", 1, ICPPField.class);
        ba.assertNonProblem("a; //4", 1, ICPPField.class);
        ba.assertNonProblem("a; //5", 1, ICPPField.class);
        ba.assertProblem("a; //6", 1);
    }
    
    
    // struct A {
	//   int x;
	// };
	//
	// struct B {
	//    A& operator++(); // prefix
	//    A operator++(int); // postfix
	// };
	//
	// void test(B p1, B p2) {
	//    (p1++).x; //1
	//    (++p1).x; //2
	// }
    public void testOverloadedUnaryOperator_259927_3() throws Exception {
    	BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertNonProblem("x; //1", 1, ICPPField.class);
    	ba.assertNonProblem("x; //2", 1, ICPPField.class);
    }
    
    
	// struct A {
	//   int x;
	// };
	// struct B { };
    // A& operator++(B); // prefix
	// A operator++(B, int); // postfix
	//
	// void test(B p1, B p2) {
	//    (p1++).x; //1
	//    (++p1).x; //2
	// }
    public void testOverloadedUnaryOperator_259927_4() throws Exception {
    	BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertNonProblem("x; //1", 1, ICPPField.class);
    	ba.assertNonProblem("x; //2", 1, ICPPField.class);
    }
    

	// struct A {
	//	int xx;
	// };
	//
	//
	// struct B {
	//	A operator*();
	//	A operator&();
	//	A operator+();
	//	A operator-();
	//	A operator!();
	//	A operator~();
	// };
	//
	// int main() {
	//	B b;
	//
	//	(*b).xx; // 1
	//	(&b).xx; // 2
	//	(+b).xx; // 3
	//	(-b).xx; // 4
	//	(!b).xx; // 5
	//	(~b).xx; // 6
	// }
    public void testOverloadedUnaryOperator_259927_5() throws Exception {
    	BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	for(int i = 1; i <=6; i++)
    		ba.assertNonProblem("xx; // "+i, 2, ICPPField.class);
    }
    
	// struct A {
	//	int xx;
	// };
	//
	//
	// struct B {
	// };
	//
	// A operator*(B);
	// A operator&(B);
	// A operator+(B);
	// A operator-(B);
	// A operator!(B);
	// A operator~(B);
	//
	// int main() {
	//	B b;
	//
	//	(*b).xx; // 1
	//	(&b).xx; // 2
	//	(+b).xx; // 3
	//	(-b).xx; // 4
	//	(!b).xx; // 5
	//	(~b).xx; // 6
	//}
    public void testOverloadedUnaryOperator_259927_6() throws Exception {
    	BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	for(int i = 1; i <=6; i++)
    		ba.assertNonProblem("xx; // "+i, 2, ICPPField.class);	
    }
    
    
    
    // int a,b,c,d ;
    // class X {
    //	 void m() {
    //      T* a;
    //		I* b;
    //		S1* c;
    //		S2* d;
    //	 }
    //	 typedef int T;
    //	 int I;
    //	 typedef int S1 (int(T));  // resolve this ambiguity first
    //	 typedef int S2 (int(t));  // resolve this ambiguity first
    // };
    public void testOrderOfAmbiguityResolution_259373() throws Exception {
        BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
        ICPPVariable a= ba.assertNonProblem("a;", 1);
        assertInstance(a.getType(), IPointerType.class);
        ICPPVariable b= ba.assertNonProblem("b;", 1);
        assertInstance(b.getType(), IBasicType.class);
        ICPPVariable c= ba.assertNonProblem("c;", 1);
        assertInstance(c.getType(), IPointerType.class);
        ITypedef s1= (ITypedef) ((IPointerType) c.getType()).getType();
        assertInstance(((IFunctionType) s1.getType()).getParameterTypes()[0], IPointerType.class);
        ICPPVariable d= ba.assertNonProblem("d;", 1);
        assertInstance(d.getType(), IPointerType.class);
        ITypedef s2= (ITypedef) ((IPointerType) d.getType()).getType();
        assertInstance(((IFunctionType) s2.getType()).getParameterTypes()[0], IBasicType.class);
    }

    //    namespace A {
    //    	class X {
    //    		friend void f(int);
    //    		class Y {
    //    			friend void g(int);
    //    		};
    //    	};
    //    	void test() { 
    //         f(1);
    //         g(1);
    //      } 
    //    }
    public void testFriendFunctionResolution_86368_1() throws Exception {
		final String code= getAboveComment();
		parseAndCheckBindings(code);
		
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		IFunction f1= bh.assertNonProblem("f(int)", 1);
		IFunction f2= bh.assertNonProblem("f(1)", 1);
		assertSame(f1, f2);
		IFunction g1= bh.assertNonProblem("g(int)", 1);
		IFunction g2= bh.assertNonProblem("g(1)", 1);
		assertSame(g1, g2);

		bh= new BindingAssertionHelper(code, true);
		f2= bh.assertNonProblem("f(1)", 1);
		f1= bh.assertNonProblem("f(int)", 1);
		assertSame(f1, f2);
		g2= bh.assertNonProblem("g(1)", 1);
		g1= bh.assertNonProblem("g(int)", 1);
		assertSame(g1, g2);
    }

    //    namespace A {
    //    	void f(int);
    //    }
    //    using A::f;
    //    namespace A {
    //    	void f(char); // openReferences fails
    //    }
    //    void foo() {
    //    	f('i');
    //    }
    //    void bar() {
    //    	using A::f;
    //    	f('c');
    //    }
    public void testFriendFunctionResolution_86368_2() throws Exception {
		final String code= getAboveComment();
		parseAndCheckBindings(code);
		
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		IFunction f1= bh.assertNonProblem("f(int)", 1);
		IFunction f2= bh.assertNonProblem("f('i')", 1);
		assertSame(f1, f2);
		IFunction g1= bh.assertNonProblem("f(char)", 1);
		IFunction g2= bh.assertNonProblem("f('c')", 1);
		assertSame(g1, g2);

		bh= new BindingAssertionHelper(code, true);
		f2= bh.assertNonProblem("f('i')", 1);
		f1= bh.assertNonProblem("f(int)", 1);
		assertSame(f1, f2);
		g2= bh.assertNonProblem("f('c')", 1);
		g1= bh.assertNonProblem("f(char)", 1);
		assertSame(g1, g2);
    }
    
    // class A {
    // public:
    //    void foo() const volatile;
    //    void foo() volatile;
    //    void foo() const;
    //    void foo();
    //    void bar() const volatile;
    //    void bar() volatile;
    //    void bar() const;
    //    void bar();
    // };
    // void A::foo() const volatile { bar();/*1*/ }
    // void A::foo() volatile       { bar();/*2*/ }
    // void A::foo() const          { bar();/*3*/ }
    // void A::foo()                { bar();/*4*/ }
    // void test() {
    //   A a;
    //   const A ca;
    //   volatile A va;
    //   const volatile A cva;
    //   cva.bar();/*5*/
    //   va.bar();/*6*/
    //   ca.bar();/*7*/
    //   a.bar();/*8*/
    // }
    public void testMemberFunctionDisambiguationByCVness_238409() throws Exception {
		final String code= getAboveComment();
		BindingAssertionHelper bh= new BindingAssertionHelper(code, true);
		
    	ICPPMethod bar_cv= bh.assertNonProblem("bar();/*1*/", 3, ICPPMethod.class);
    	ICPPMethod bar_v=  bh.assertNonProblem("bar();/*2*/", 3, ICPPMethod.class);
    	ICPPMethod bar_c=  bh.assertNonProblem("bar();/*3*/", 3, ICPPMethod.class);
    	ICPPMethod bar=    bh.assertNonProblem("bar();/*4*/", 3, ICPPMethod.class);
    	ICPPFunctionType bar_cv_ft= bar_cv.getType();
    	ICPPFunctionType bar_v_ft=  bar_v.getType();
    	ICPPFunctionType bar_c_ft=  bar_c.getType();
    	ICPPFunctionType bar_ft=    bar.getType();
    	
    	assertTrue(bar_cv_ft.isConst()); assertTrue(bar_cv_ft.isVolatile());
    	assertTrue(!bar_v_ft.isConst()); assertTrue(bar_v_ft.isVolatile());
    	assertTrue(bar_c_ft.isConst());  assertTrue(!bar_c_ft.isVolatile());
    	assertTrue(!bar_ft.isConst());   assertTrue(!bar_ft.isVolatile());

    	bar_cv= bh.assertNonProblem("bar();/*5*/", 3, ICPPMethod.class);
    	bar_v=  bh.assertNonProblem("bar();/*6*/", 3, ICPPMethod.class);
    	bar_c=  bh.assertNonProblem("bar();/*7*/", 3, ICPPMethod.class);
    	bar=    bh.assertNonProblem("bar();/*8*/", 3, ICPPMethod.class);
    	bar_cv_ft= bar_cv.getType();
    	bar_v_ft=  bar_v.getType();
    	bar_c_ft=  bar_c.getType();
    	bar_ft=    bar.getType();
    	
    	assertTrue(bar_cv_ft.isConst()); assertTrue(bar_cv_ft.isVolatile());
    	assertTrue(!bar_v_ft.isConst()); assertTrue(bar_v_ft.isVolatile());
    	assertTrue(bar_c_ft.isConst());  assertTrue(!bar_c_ft.isVolatile());
    	assertTrue(!bar_ft.isConst());   assertTrue(!bar_ft.isVolatile());
    }
    
    //    void test1(float f);
    //    void test1(void);
    //    void blabla() {
    //    	test1(1);
    //    }
    //    enum E {e1};
    //    class C {};
    //    void test2(float f);
    //    void test2(C c);
    //    void blabla2() {
    //    	test2(e1);
    //    }
    public void testOverloadResolution_262191() throws Exception {
		final String code= getAboveComment();
		parseAndCheckBindings(code);
    }

    //	enum E {e1};
    //	typedef int TInt;
    //	void select(int);
    //	void test() {
    //    int a= TInt(1); 
    //    E e= E(0);
    //    void* h;
    //    select (int (h) + 1);
    //  }  
    public void testSimpleTypeConstructorExpressions() throws Exception {
		final String code= getAboveComment();
    	parseAndCheckBindings(code);
    }
	
	//	struct A {};
	//	A a();
	//
	//	void f(const int& x);
	//
	//	void test() {
	//	  f(a());
	//	}
	public void testBug263152_1() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("f(a())", 1);
	}

	//	struct A {};
	//	A a();
	//
	//	struct B {
	//	  void m(const A& x);
	//	  void m(const B& x);
	//	};
	//
	//	void test(B p) {
	//	  p.m(a());
	//	}
	public void testBug263152_2() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("m(a())", 1, ICPPMethod.class);
	}

	//	struct A {
	//	  int a;
	//	  static void m() {
	//	    a = 0;
	//	  }
	//	};
	public void _testInstanceMemberInStaticMethod_263154() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("a =", 1);
	}

	//	struct A {};
	//	void test() {
	//		int B, b;
	//		while (A* a = 0);
	//		while (A* a = 0) {a= 0;}
	//		while (B* b) {b= 0;}
	//		if (A* a = 0) {a= 0;}
	//		if (B* b) {b= 0;}
	//		switch (A* a = 0) {case 1: a=0;}
	//		switch (B* b) {case1: b= 0;}
	//		for (;A* a = 0;) {a= 0;}
	//		for (;B* b;) {b= 0;}
	//	}
    public void testAmbiguityResolutionInCondition_263158() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ba.assertNonProblem("A*", 1, ICPPClassType.class);
		ba.assertNonProblem("a", 1, ICPPVariable.class);
		ba.assertNonProblem("B*", 1, ICPPVariable.class);
		
		parseAndCheckBindings(code, ParserLanguage.CPP);
    }

	//	void f(int x);
	//
	//	void test(int* p, const int* q, int r[], const int s[]) {
	//	  f(p);
	//	  f(q);
	//	  f(r);
	//	  f(s);
	//	}
	public void testPointerToNonPointerConversion_263159() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("f(p)", 1);
		ba.assertProblem("f(q)", 1);
		ba.assertProblem("f(r)", 1);
		ba.assertProblem("f(s)", 1);
	}

	//	void fip(int* x);
	//	void fia(int x[]);
	//
	//	void test() {
	//	  fip(1);
	//	  fia(1);
	//	  fip(0);
	//	  fia(0);
	//	}
	public void testNonPointerToPointerConversion_263707() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertProblem("fip(1)", 3);
		ba.assertProblem("fia(1)", 3);
		ba.assertNonProblem("fip(0)", 3, ICPPFunction.class);
		ba.assertNonProblem("fia(0)", 3, ICPPFunction.class);
	}
	
	//	class cl {};
	//	typedef cl tl;
	//	void reset(cl* ptr){};
	//	void blatest() {
	//	   reset(new tl());
	//	   reset(new cl());
	//     reset(new cl[1]);
	//	}
	public void testTypeOfNewExpression_264163() throws Exception {
		String code= getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	template<typename IteratorT> class range {
	//	public:
	//		template<class Range> range(const Range& r) {}
	//	};
	//	void onRange(const range<const char*>& r) {}
	//	void test() {
	//		range<char*> ir(0);
	//		onRange(ir);
	//	}
	public void testConstructorTemplateInImplicitConversion_264314() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ba.assertNonProblem("onRange(ir)", 7);
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	typedef int * pi;
	//	typedef int *const* pcpi;
	//	typedef const pi* pcpi2;
	//	void check(pcpi) {};
	//	void testxxx() {
	//	        pcpi p1;
	//	        pcpi2 p2;
	//	        check(p1);
	//	        check(p2);
	//	}
	//  template<typename T> class CT {};
	//  CT<pcpi> ct1;
	//  CT<pcpi2> ct2;
	public void testConstTypedef_264474() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ba.assertNonProblem("check(p2)", 5);
		IBinding ct1= ba.assertNonProblem("CT<pcpi>", 8);
		IBinding ct2= ba.assertNonProblem("CT<pcpi2>", 9);
		assertSame(ct1, ct2);
		
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	class X {
	//		public:
	//			int f;
	//	void m(int) {};
	//	void cm(int) const {};
	//	static int sf;
	//	static void sm(int) {};
	//	};
	//	int X::sf;
	//
	//	void mpr(int X::* p){}
	//	void mpr(void (X::* q)(int)){}
	//	void mprc(void (X::* q)(int) const){}
	//	void mprcp(void (X::** q)(int) const){}
	//	void pr(int * p){}
	//	void pr(void (*q)(int)){}
	//
	//	void testxxx() {
	//		void (X::* ptr)(int) const= &X::cm;
	//		mpr(&X::f);
	//		mpr(&X::m);
	//		mprc(&X::cm);
	//		mprcp(&ptr);
	//	    pr(&X::sf);
	//	    pr(&(X::sf));
	//	    pr(&X::sm);
	//	    pr(&(X::sm));
	//
	//		// invalid constructs:
	//		mpr(&(X::f));  // cannot use parenthesis
	//		mpr(&(X::m));   // cannot use parenthesis
	//		mpr(&X::sf);    // sf is static
	//		mpr(&X::sm);    // sm is static
	//		mpr(&X::cm);    // cm is const
	//		mprc(&X::m);    // m is not const
	//	}
	public void testMemberPtrs_264479() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ba.assertNonProblem("mpr(&X::f)", 3);
		ba.assertNonProblem("mpr(&X::m)", 3);
		ba.assertNonProblem("mprc(&X::cm)", 4);
		ba.assertNonProblem("mprcp(&ptr)", 5);
		ba.assertNonProblem("pr(&X::sf)", 2);
		ba.assertNonProblem("pr(&(X::sf))", 2);
		ba.assertNonProblem("pr(&X::sm)", 2);
		ba.assertNonProblem("pr(&(X::sm))", 2);

		ba.assertProblem("mpr(&(X::f))", 3);
		ba.assertProblem("mpr(&(X::m))", 3);
		ba.assertProblem("mpr(&X::sf)", 3);
		ba.assertProblem("mpr(&X::sm)", 3);
		ba.assertProblem("mpr(&X::cm)", 3);
		ba.assertProblem("mprc(&X::m)", 4);
	}

	//	void f(int x);
	//
	//	void test(int* p) {
	//	  f(!p);
	//	}
	public void testTypeOfNotExpression_265779() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
		ba.assertNonProblem("f(!p)", 1);
	}
	
	//	class S {
	//		S(int);
	//	};
	//	void test() {
	//		S **temp = new S*[1]; // problem on S
	//		temp = new S*;        // problem on S
	//		temp = new (S*);      // problem on S
	//		temp = new ::S*[1]; // problem on S
	//		temp = new ::S*;        // problem on S
	//		temp = new (::S*);      // problem on S
	//	}
	public void testNewPointerOfClass_267168() throws Exception {
		final String code = getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	void f(char *(names[2])= 0);
	//  void f2(const char *(n[])) {
	//     if (n && 1){}
	//  }
	public void testPointerToArrayWithDefaultVal_267184() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ICPPParameter p= ba.assertNonProblem("names", 5);
		assertTrue(p.hasDefaultValue());
		IType t= p.getType();
		assertInstance(t, IPointerType.class);  // parameter of type array is converted to pointer
		t= ((IPointerType) t).getType();
		assertInstance(t, IPointerType.class);
		t= ((IPointerType) t).getType();
		assertInstance(t, IBasicType.class);
		
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	// class X {
	//    virtual void pv() = 0;
	//    void (*ptrToFunc) ()= 0;
	// };
	public void testPureVirtualVsInitDeclarator_267184() throws Exception {
		final String code = getAboveComment();
		IASTTranslationUnit tu= parseAndCheckBindings(code, ParserLanguage.CPP);
		ICPPASTCompositeTypeSpecifier ct= getCompositeType(tu, 0);
		IASTSimpleDeclaration sdecl= getDeclaration(ct, 0);
		ICPPASTFunctionDeclarator dtor= (ICPPASTFunctionDeclarator) sdecl.getDeclarators()[0];
		assertTrue(dtor.isPureVirtual());
		assertNull(dtor.getInitializer());

		sdecl= getDeclaration(ct, 1);
		dtor= (ICPPASTFunctionDeclarator) sdecl.getDeclarators()[0];
		assertFalse(dtor.isPureVirtual());
		assertNotNull(dtor.getInitializer());

		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	namespace ns {
	//		struct S {
	//			int a;
	//		};
	//	}
	//	class A {
	//		public:
	//			operator ns::S*(){return 0;};
	//	};
	//	namespace ns {
	//		void bla() {
	//			A a;
	//			a.operator S *();
	//		}
	//	}
	public void testLookupScopeForConversionNames_267221() throws Exception {
		final String code = getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	
	//	struct A {int a;};
	//
	//	int foo();
	//	int bar(A);
	//
	//	int func() {
	//		bar((A){foo()});
	//	}
	public void testBug268714() throws Exception {
		final String code = getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	void f(int volatile * const * ) {}
	//	void f(int const volatile * const *) {}
	//	void test() {
	//	   int ** x;
	//	   f(x);  // problem binding here
	//	}
	public void testRankingOfQualificationConversion_Bug269321() throws Exception {		
		final String code = getAboveComment();
		parseAndCheckBindings(code, ParserLanguage.CPP);
	}
	
	//	class C {
	//		C& operator()() {return *this;}
	//	};
	//	void test() {
	//		C c;
	//		c()()()()()()()()()()()()()();
	//	}
	public void testNestedOverloadedFunctionCalls_Bug283324() throws Exception {
		final String code = getAboveComment();
		IASTTranslationUnit tu= parseAndCheckBindings(code, ParserLanguage.CPP);
		IASTFunctionDefinition test= getDeclaration(tu, 1);
		IASTExpressionStatement stmt= getStatement(test, 1);
		long now= System.currentTimeMillis();
		IType t= stmt.getExpression().getExpressionType();
		assertInstance(t, ICPPReferenceType.class);
		final long time = System.currentTimeMillis() - now;
		assertTrue("Lasted " + time + "ms", time < 5000);
	}
	
	
	//	struct A { int a; };
	//	struct B { int b; };
	//
	//	struct X {
	//	    A operator+(int);
	//	    X(int);
	//	};
	//
	//	A operator+(X,X);
	//	B operator+(X,double);
	//
	//	void test(X x) {
	//	    (x + 1).a; //1
	//	    (1 + x).a; //2
	//	    (x + 1.0).b; //3
	//	}
	public void testOverloadResolutionForOperators_Bug266211() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertNonProblem("a; //1", 1, ICPPField.class);
    	ba.assertNonProblem("a; //2", 1, ICPPField.class);
    	ba.assertNonProblem("b; //3", 1, ICPPField.class);
	}
	
	
	//	struct A { int a; };
	//	struct X {
	//	    A operator+(X);
	//	    void m();
	//	};
	//
	//	A operator+(X,double);
	//
	//	void X::m() {
	//	    X x;
	//	    (x + x).a; //1
	//	    (x + 1.0).a; //2
	//	}
	public void testOverloadResolutionForOperators_Bug268534() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertNonProblem("a; //1", 1, ICPPField.class);
    	ba.assertNonProblem("a; //2", 1, ICPPField.class);
	}

	//	class C {
	//		operator int(){return 0;}
	//	};
	//	void test(int) {}
	//	void ref() {
	//		C* c= 0;
	//		test(c);
	//	}
	public void testInvalidUserDefinedConversion_Bug269729() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertProblem("test(c)", 4);
	}
	

	//	int foo(char * x);
	//	int foo(wchar_t * x);
	//  int foo(char x);
	//	int foo(wchar_t x);
	//
	//	int main() {
	//		foo("asdf");
	//		foo(L"asdf");
	//		foo('a');
	//		foo(L'a');
	//	}
	public void testWideCharacterLiteralTypes_Bug270892() throws Exception {
		IASTTranslationUnit tu = parse( getAboveComment(), ParserLanguage.CPP ); 
		CPPNameCollector col = new CPPNameCollector();
		tu.accept(col);
		
		assertSame(col.getName(0).resolveBinding(), col.getName(9).resolveBinding());
		assertSame(col.getName(2).resolveBinding(), col.getName(10).resolveBinding());
		assertSame(col.getName(4).resolveBinding(), col.getName(11).resolveBinding());
		assertSame(col.getName(6).resolveBinding(), col.getName(12).resolveBinding());
	}

	//	namespace ns {
	//	  struct A {};
	//	}
	//	using ns::A;
	//	struct A;
	//	A a;
	public void testForwardDeclarationAfterUsing_271236() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ba.assertNonProblem("A a;", 1, ICPPClassType.class);
	}
	
	//	template <class T> class Moo;
	//	bool getFile(Moo <class Foo> & res);
	public void testScopeOfClassFwdDecl_270831() throws Exception {
		BindingAssertionHelper ba= new BindingAssertionHelper(getAboveComment(), true);
    	ICPPClassType t= ba.assertNonProblem("Foo", 3, ICPPClassType.class);
    	IScope scope= t.getScope();
    	assertEquals(EScopeKind.eGlobal, scope.getKind());
	}
	
	//	class C {};
	//	class D : public C {};
	//	class E {
	//	   public: E(D) {}
	//	};
	//	void test(C c) {}
	//	void test(E e) {}
	//
	//	void xx() {
	//	   D d1;
	//	   const D d2= D();
	//	   test(d1); // problem binding here although test(C c) has to be selected
	//	   test(d2); // problem binding here although test(C c) has to be selected
	//	}
	public void testDerivedToBaseConversion_269318() throws Exception {
		final String code = getAboveComment();
		BindingAssertionHelper ba= new BindingAssertionHelper(code, true);
		ICPPFunction t= ba.assertNonProblem("test(d1);", 4, ICPPFunction.class);
		ICPPClassType ct= (ICPPClassType) t.getParameters()[0].getType();
    	assertEquals("C", ct.getName());

		t= ba.assertNonProblem("test(d2);", 4, ICPPFunction.class);
		ct= (ICPPClassType) t.getParameters()[0].getType();
    	assertEquals("C", ct.getName());

    	parseAndCheckBindings(code, ParserLanguage.CPP);
	}

}
