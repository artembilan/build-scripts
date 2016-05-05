package io.springframework.cloud.ci

import io.springframework.common.JdkConfig
import io.springframework.common.Notification
import javaposse.jobdsl.dsl.DslFactory

/**
 * @author Marcin Grzejszczak
 */
class DocsAppBuildMaker implements Notification, JdkConfig {
	private final DslFactory dsl

	DocsAppBuildMaker(DslFactory dsl) {
		this.dsl = dsl
	}

	void buildDocs(String cronExpr) {
		dsl.job('spring-cloud-sleuth-docs-apps-ci') {
			triggers {
				cron cronExpr
			}
			jdk jdk8()
			scm {
				git {
					remote {
						url "https://github.com/spring-cloud-samples/sleuth-documentation-apps"
						branch 'master'
					}
					createTag(false)
				}
			}
			steps {
				gradle('clean build --parallel')
			}

			configure {
				appendSlackNotificationForSpringCloud(it as Node)
			}
		}
	}
}