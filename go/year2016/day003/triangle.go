package main

type Triangle struct {
	A int
	B int
	C int
}

func (t Triangle) IsValid() bool {
	return t.A+t.B > t.C && t.A+t.C > t.B && t.B+t.C > t.A
}
