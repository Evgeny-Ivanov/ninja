module.exports = function (grunt) {

    grunt.initConfig({
        shell: {
            options: {
                stdout: true,
                stderr: true
            },
            server: {
                command: 'java -jar server.jar 8080'
            }
        },
		requirejs: {
            build: {
                options: {
                    almond: true,
                    baseUrl: "static/js",
                    mainConfigFile: "static/js/main.js",
                    name: "main",
                    optimize: "none",
                    out: "static/js/build/main.js"
                }
            }
        },
        concat: {
            build: { /* Подзадача */
                separator: ';\n',
                src: [
                      'static/js/lib/almond.js',
                      'static/js/build/main.js'
                ],
                dest: 'static/js/build.js'
            }
        }, 
        uglify: { 
            build: { /* Подзадача */
                files: {
                    'static/js/build.min.js': 
                          ['static/js/build.js']
                }
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
            style: "compressed",
            dist: {
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
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');

    grunt.registerTask(
    'build',
    [
        'fest', 'requirejs:build',
        'concat:build', 'uglify:build'
    ]
    );

    grunt.registerTask('default', ['concurrent']);

};