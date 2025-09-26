package com.rajanlad.immersiv_play.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.xr.compose.spatial.*
import androidx.xr.compose.subspace.*
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.*
import androidx.xr.runtime.Session
import androidx.xr.runtime.math.Pose
import androidx.xr.runtime.math.Quaternion
import androidx.xr.runtime.math.Vector3
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.GltfModelEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch
import java.nio.file.Paths


@OptIn(ExperimentalSubspaceVolumeApi::class)
@Composable
fun ModelInsideVolume(session: Session) {
    val modelState = remember { mutableStateOf<GltfModelEntity?>(null) }

    // to tranform the model as needed with Pose
    val translation = Vector3(0f, -0.6f, 0.5f)

    val rotation = Quaternion.fromAxisAngle(Vector3.Up,90f)

    val pose = Pose(translation, rotation)

    LaunchedEffect(session) {
        val model = GltfModel.create(session, Paths.get("models", "football_field.glb"))
        val entity = GltfModelEntity.create(session, model, pose = pose)
        entity.setScale(0.03f)

        modelState.value = entity
    }

    // load the the Entity into Volume as a child
    Subspace {
        SpatialColumn {
            Volume(
                modifier = SubspaceModifier
                    .scale(0.5f)
                    .size(100.dp)
            ) { parent ->
                modelState.value?.let { entity ->
                    parent.addChild(entity)
                }
            }
        }
    }
}
