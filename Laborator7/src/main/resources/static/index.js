const application = {
    run: function() {
        async function loadTemplate(view) {
            let response = await fetch(view);
            let text = await response.text();
            return Handlebars.compile(text);
        }
        function render (template, data) {
            document.getElementById('main').innerHTML = template(data);
        }
        async function listTasks() {
            let response = await fetch ('/api/tasks', {
                method: 'GET',
                headers : {
                    'X-Fields' : 'id,title,status',
                    'X-Sort': 'status'
                }
            });
            let tasks = await response.json();
            let data =  tasks.reduce((result, task) => {
                if(!result[task.status]) {
                    result[task.status] = [];
                }
                result[task.status].push(task);
                return result;
            }, {});
            render(await loadTemplate('tasks.html'), {groups : data});
        }
        listTasks();
    }
};