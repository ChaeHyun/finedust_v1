package ch.breatheinandout.dependencyinjection

import dagger.hilt.migration.AliasOf
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@AliasOf(Singleton::class)
annotation class AppScoped