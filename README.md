CampusGlass
===========

People can pick up campus glass where there are some messages witten by others ,then they talk by send messages in the app.
```flow
st=>start: Start
op=>operation: Your Operation
op1=>operation: HaHa
cond1=>condition: ok or not?
cond=>condition: Yes or No?
e=>end

st->op->op1->cond1
cond1(yes)->cond
cond1(no)->op
cond(yes)->e
cond(no)->op
```
