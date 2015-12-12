module.exports = function (grunt) {

    grunt.initConfig({
        shell: {
            options: {
                stdout: true,
                stderr: true
            },
            server: {
                command: 'java -cp server.jar main.Main 8080'
            }
        },
        fest: {
            templates: {
                files: [{
                    expand: true,

                    cwd: 'templates',/* исходная директория */
                    src: '*.xml',/* имена шаблонов */
                    dest: 'static/js/tmpl'/* результирующая директория */
                }],
                options: {
                    template: function (data) {/* формат функции-шаблона */
                        return grunt.template.process(/* присваиваем функцию-шаблон переменной */
                            'define(function () { return <%= contents %> ; });',
                            {data: data}
                        );
                    }
                }
            }
        },
        watch: {
            fest: {
                files: ['templates/*.xml'],
                tasks: ['fest'],
                options: {
                    interrupt: true,
                    atBegin: true
                }
            },
            server: {
                files: [
                    'static/js/**/*.js',
                    'static/css/**/*.css'
                ],
                options: {
                    livereload: true
                }
            },
            sass: {
                files: [
                    'static/sass/**/*.scss'
                ],
                tasks: ['sass'],
                options: {
                    livereload: true
                }
            }
        },
        sass: {
            dist: {
                /*files: [{
                    expand: true,
                    cwd: 'public_html/sass', // исходная директория 
                    src: ['*.scss'], // имена шаблонов 
                    dest: 'public_html/css', // результирующая директория 
                    ext:  '.css'
                }]*/
                files: {
                    'static/css/head.css': 'static/sass/head.scss'
                }
            }
        },
        concurrent: {
            target: [ 'watch','shell','sass'],
            options: {
                logConcurrentOutput: true /* Вывод процесса */
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-concurrent');
    grunt.loadNpmTasks('grunt-shell');
    grunt.loadNpmTasks('grunt-fest');
    grunt.loadNpmTasks('grunt-sass');


    grunt.registerTask('default', ['concurrent']);

};