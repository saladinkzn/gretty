package org.akhikhl.gretty
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

import java.nio.file.Paths
/**
 *
 */
class PrepareInplaceWebAppFolderTask extends DefaultTask {
  @InputDirectory
  def File inputDir

  @OutputDirectory
  def File outputDir

  @Input
  def inplaceMode

  @TaskAction
  def execute(IncrementalTaskInputs inputs) {
    // TODO: is this can be implemented in another way?
    if(this.inputs.properties.inplaceMode == 'hard') {
      return
    }
    println("Input folder: ${inputDir.absolutePath}")
    println("Output folder: ${outputDir.absolutePath}")
    println inputs.incremental ? "CHANGED inputs considered out of date" : "ALL inputs considered out of date"


    if(!inputs.incremental) {
      ProjectUtils.prepareInplaceWebAppFolder(project)
      return;
    }

    def outOfDateCopied  = 0
    def inputPath = Paths.get(inputDir.absolutePath)
    inputs.outOfDate {
      def changedParentPath = Paths.get(it.file.parentFile.absolutePath)
      def relativePath = inputPath.toUri().relativize( changedParentPath.toUri() ).toString()
      project.copy {
        from "$inputDir/$relativePath"
        into "$outputDir/$relativePath"
      }
      outOfDateCopied++
    }
    println "Copied: ${outOfDateCopied}"
    def removed = 0
    inputs.removed {
      def removedPath = Paths.get(it.file.absolutePath)
      def relativePath = inputPath.toUri().relativize( removedPath.toUri() ).toString()
      def targetFile = project.file("$outputDir/${relativePath}")
      targetFile.delete()
      removed++
    }
    println "Deleted: ${removed}"
  }

}
