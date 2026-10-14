# PROJECT_STATE — AuraShow

Last reviewed: **2026-10-13 (America/New_York)**. Continuity schema: **1**.

This is a maintained project handoff, not an assertion that every historical chat or runtime behavior has been verified. Recheck Git and source at session start. Architecture and feature notes below describe the audited local project unless explicitly labeled remote default.

## End goal

An Android ambient slideshow with user-selected images, clock/weather overlays, and weather effects; charging-triggered behavior is a product goal, not established as complete.

## Architecture

Kotlin Android app; slideshow and weather view models, preferences, Compose-style UI components and weather-effect renderers.

## Decisions and workflow

- Current authorization (2026-10-13): user explicitly requested consolidating these project branches into main and deleting the old branches. This supersedes earlier backup-only/no-merge restrictions for this consolidation. It does not authorize deployment, personal-data changes, or claiming unperformed runtime acceptance.

- Keep weather credentials outside committed source.
- Treat README architectural pillars as intended scope until confirmed by implementation and runtime checks.
- Historical authorization, superseded for consolidation: Preserve existing acceptance gates and uncommitted work. The 2026-10-12 authorization covers continuity documentation branches only, not merging features, deploying, or pushing unrelated code.

## Current status

2026-10-13 main consolidation: verified source from `main` at `61f0039a47fbd399ecdf7a5a95dd8f7564f6ec33` is promoted to `main` in https://github.com/ZuruiKyoku/AuraShow. Application source is unchanged by consolidation. All previously advertised branches are preserved in main ancestry before deletion; main becomes the default and sole remote branch. The task receipt records the final commit and remote verification. Prior source-backup exclusions remain in force. Local private/generated data is retained on disk. Runtime acceptance gaps below remain open.

## Completed work

These are source/history findings; historical test reports are labeled and are not fresh runtime verification.

- Source includes slideshow, settings, image-fit/transition controls, weather service and individual weather effects.
- README describes the project as work in progress; Git history records duration-step and scrollbar fixes.

## Known issues and gaps

- No project-specific ChatGPT transcript was returned in the bounded chat inventory.
- Current transition choices, charging activation, permissions and live weather behavior have not been runtime-tested in this audit.
- No directly associated local coding transcript was found in the reviewed inventory. Project goals come from source/README unless explicitly marked inferred.
- ChatGPT discovery returned a bounded recent list, not every project conversation. Some retrieved assistant messages contain only content-reference placeholders; attachments and unavailable responses were not reconstructed.

## Roadmap

- Run slideshow, transitions and weather on a device; confirm charging-trigger requirements.
- Recover the AuraShow project chats to reconcile the roadmap.
- Reconcile the missing historical context before treating this document as a complete replacement for old chats.

## Next steps

1. Read this file and applicable `AGENTS.md`; inspect the current branch, working tree and remote refs.
2. Run slideshow, transitions and weather on a device; confirm charging-trigger requirements.
3. Perform the project's required checks for any code change, obtain existing acceptance where required, and update this handoff with actual results.

## Verification and evidence

**Main consolidation (2026-10-13):** all old remote branch tips checked for ancestry; merged tree checked against the verified source snapshot with only continuity documentation allowed to differ. Remote main/default/branch-deletion checks are recorded in the final task receipt. Existing build/runtime evidence below is historical; no application code was changed by consolidation.

- This session verified repository identity, local Git metadata/tracked paths, live remote refs where configured, and inspected documentation/source evidence. It changed documentation only.
- No application build, unit-test suite, device test, deployment check or live-service test is implied by this audit. Prior successful checks do not validate later uncommitted edits.
- Evidence labels: **source present** is static evidence; **historical report** is a past session claim; **verified now** requires an explicitly recorded current check; **planned/unknown** is not implemented.

- `app/src/main/java/com/slygames/aurashow/ui/components/Slideshow.kt` — present on remote default; same content locally.
- `app/src/main/java/com/slygames/aurashow/data/WeatherService.kt` — present on remote default; same content locally.

### History sources and coverage

No directly associated local coding transcript was found in the reviewed inventory. Project goals come from source/README unless explicitly marked inferred.

- No matching local transcript identified.

## Session handoff

User authorized merge/promotion to main and deletion of old branches on 2026-10-13. Canonical destination: https://github.com/ZuruiKyoku/AuraShow, main. Source baseline: 61f0039a47fbd399ecdf7a5a95dd8f7564f6ec33. Existing branch histories are included before deletion; the application tree remains the verified source snapshot. Local checkout alignment must preserve original file bytes except this handoff, as well as private/generated files. For Prioritizer and the Godot prototype, the private snapshot repository stays canonical; original public repositories are not modified. No deployment or new feature phase is started. Consult Known issues and gaps for outstanding manual/runtime checks; branch consolidation is not proof that those passed.
