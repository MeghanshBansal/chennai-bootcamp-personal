Vue.component('signup', {
    template:
        `
        <div class="col-sm-6">
            <form>
              <div class="form-group">
                <label for="textName">Name</label>
                <input type="text" class="form-control" id="textName" placeholder="Name" maxlength="15" v-model="name" required>
                <small id="nameHelp" class="form-text text-muted">Input your name</small>
              </div>

              <div class="form-group">
                <label for="inputEmail1">Email address</label>
                <input type="email" class="form-control" id="inputEmail1" aria-describedby="emailHelp" placeholder="Enter email" v-model="email" maxlength="50" required>
                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
              </div>
              <button type="submit" class="btn btn-dark" v-on:click.prevent="onSubmit">Register</button>
              <div>
                <p class="text-danger" v-if='response!==""'>{{response}}</p>
              </div>
            </form>   
        </div>                
    `,
    data() {
        return {
            name: '',
            email: '',
            response: ''
        }
    },
    methods: {
        onSubmit() {
            var temp = {
                "name": this.name,
                "email": this.email
            };
            if (this.name !== '' && this.email !== '') {

                axios.post("/SignUp", temp, {headers: {"content-type": "application/json"}}).then(result => {
                    this.response = "Signed up the user " + this.name;
                    this.name = '';
                    this.email = '';
                }, error => {
                    this.response = "Some error while signing up user";
                    console.error(error);
                });
            } else {
                this.response = "please fill all the fields"
            }
        }
    }
});

Vue.component('login', {
    template:
        `
    <div class="col-sm-6">
        <form>
            <div class="form-group">
                <label for="inputEmailLogin">Email address</label>
                <input type="email" class="form-control" id="inputEmailLogin" aria-describedby="emailHelp" placeholder="Enter email" v-model="email"/>
            </div>

            <div v-if='otpSent' class="form-group">
                <label for="inputOTP">OTP</label>
                <input type="password" class="form-control" id="inputOTP" v-model="otp" pattern="[0-9]{4}" maxlength="4" />
            </div>

            <div v-if='!otpSent'>
                <button class="btn btn-dark" v-on:click.prevent="onEmailSubmit">Send OTP</button>
            </div>
            <div v-if='otpSent'>
                <button class="btn btn-dark" v-on:click.prevent="onOTPSubmit">Validate OTP</button>
            </div>
            <div>
                <p class="text-danger" v-if='response!==""'>{{response}}</p>
            </div>
        </form>   
    </div>      
    `,
    data() {
        return {
            otp: '',
            email: '',
            otpSent: false,
            response: '',
            verifiedOTP: false,
        }
    },
    methods: {
        onEmailSubmit() {
            var temp = {
                "email": this.email
            };
            if (this.email !== '') {
                axios.post("/SendOTP", temp, {headers: {"content-type": "application/json"}}).then(result => {
                    this.response = "Sent OTP to " + this.email;
                    if (result.data.success === "true") {
                        this.otpSent = true;
                    }
                }, error => {
                    this.response = "Invalid Email";
                    console.error(error);
                });
            } else {
                this.response = "please fill all the fields"
            }

        },
        onOTPSubmit() {
            var temp = {
                "email": this.email,
                "otp": this.otp
            };
            if (this.email !== '' && this.otp !== '') {
                axios.post("/ValidateOTP", temp, {headers: {"content-type": "application/json"}}).then(result => {
                    if (result.data.success === "true") {
                        window.$cookies.set('token', result.data.token, {expires: '1Y', domain: 'localhost'});
                        this.verifiedOTP = true;
                        this.response = "User Verified";
                        window.location.reload(true);
                    } else {
                        this.response = result.data.error;
                    }
                }, error => {
                    this.response = "Invalid OTP or Email";
                    console.error(error);
                });
            } else {
                this.response = "please fill all the fields"
            }

        }
    }
});

Vue.component('profile', {
    props: ["name", "email"],
    template:
        `
    <li class="nav-item dropdown nav-item-pad-right">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <i class="fa fa-user-circle fa-inverse fa-1x"></i>&nbsp;&nbsp;{{ name }}
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <a class="dropdown-item" href="#"><i class="fa fa-envelope fa-fw text-dark"></i> {{ email }}</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="#" v-on:click.prevent="onLogout"><i class="fa fa-power-off fa-fw text-dark"></i> Logout</a>
        </div>
    </li>
    `,
    methods: {
        onLogout() {
            window.$cookies.remove('token');
            window.location.reload(true);
        }
    }
});

Vue.component('todos', {
    props: ["id", "name"],
    data() {
        return {
            todos: [],
            todo: "",
            error: "",
            query: "",
            queried: false
        }
    },
    template:
        `
    <div class="col-sm-6">
        <div class="row">
            <nav aria-label="breadcrumb" class="row-margin-top" style="width:100%;">
                <ol class="breadcrumb bg-dark">
                    <li class="breadcrumb-item"><a href="#">Home</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Todos</li>
                </ol>
            </nav>
        </div>
        <div class="row">
            <div class="input-group mb-3">
              <input type="text" class="form-control" placeholder="Todos search..." v-on:change="onQueryChange" v-on:input="onQueryChange"  v-model="query"/>
              <div class="input-group-append">
                <button class="btn btn-dark" v-on:click.prevent="onSearch">Search</button>
              </div>
            </div>
        </div>
        <div class="row" v-if='queried && query!==""'><p class="text-primary">Search result matching <b>{{query}}</b></p></div>
        <div class="row">
            <div class="col-sm-12">
                <ul class="list-group add-space">
                    <li class="list-group-item" v-for="todo in todos">
                        <i class="fa fa-check-square fa-fw text-dark" aria-hidden="true"></i>&nbsp;<span v-html="todo"></span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="input-group mb-3">
              <input type="text" class="form-control" placeholder="Add Todo..." v-model="todo"/>
              <div class="input-group-append">
                <button class="btn btn-dark" v-on:click.prevent="onAdd">Adding</button>
              </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <p class="text-center text-danger" v-html="error"></p>
            </div>
        </div>
    </div>
    `,
    methods: {
        onQueryChange() {
            this.queried = false;
        },
        getToDo() {
            axios.get('/GetToDo', {params: {id: this.id}}, {headers: {"content-type": "application/json"}})
                .then(result => {
                    this.todos = result.data.todos;
                }, error => {
                    console.error(error)
                });
        },
        onSearch() {
            if (this.query !== '') {
                axios.get('/search', {
                    params: {
                        id: this.id,
                        q: this.query
                    }
                }, {headers: {"content-type": "application/json"}})
                    .then(result => {
                        this.todos = result.data.todos;
                        this.queried = true;
                    }, error => {
                        console.error(error)
                    });
            }
        },
        onAdd() {
            if (this.todo !== '') {
                var url = '/CreateToDo?id=' + this.id;
                var body = {
                    "user": this.name,
                    "message": this.todo
                };
                axios.post(url, body, {headers: {"content-type": "application/json"}})
                    .then(result => {
                        if (result.data.success === 'true') {
                            this.todos.push(this.todo);
                            this.todo = "";
                            this.error = "";
                        }
                    }, error => {
                        this.error = "error while submitting new todo";
                    });
            }
        }
    },
    mounted() {
        this.getToDo();
    }
});

var home = new Vue({
    el: '#home',
    data: {
        name: '',
        email: '',
        id: '',
        loggedIn: false
    },
    methods: {
        getUser: function () {
            axios.get('/user', {}, {headers: {"content-type": "application/json"}})
                .then(result => {
                    if (result.data.success !== 'false') {
                        this.email = result.data.email;
                        this.name = result.data.name;
                        this.id = result.data.id;
                        this.loggedIn = true;
                    }
                }, error => {
                    console.error(error)
                });
        }
    },
    mounted() {
        this.getUser();
    }
});